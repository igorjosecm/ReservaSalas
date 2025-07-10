package dao;

import classes.Reserva;
import classes.ReservaDetalhada;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static helpers.TypeConverter.toLocalDateTime;

public class ReservaDAO extends GenericDAO<Reserva, Integer> {

    public ReservaDAO(Driver driver) {
        super(driver);
    }

    /**
     * Cria um nó de Reserva e seus relacionamentos com Sala, Professor e Matéria
     * em uma única consulta atômica, garantindo a integridade dos dados.
     */
    @Override
    public void create(Reserva entity) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (s:Sala {codigo_sala: $codigoSala}) " +
                        "MATCH (p:Professor {matricula_professor: $matricula}) " +
                        "MATCH (m:Materia {codigo_materia: $codigoMateria}) " +
                        "CREATE (r:Reserva $props) " +
                        "CREATE (r)-[:FEITA_PARA]->(s) " +
                        "CREATE (r)-[:REALIZADA_POR]->(p) " +
                        "CREATE (r)-[:REFERENTE_A]->(m)";

                tx.run(cypher, Values.parameters(
                        "codigoSala", entity.getCodigoSala(),
                        "matricula", entity.getMatriculaProfessor(),
                        "codigoMateria", entity.getCodigoMateria(),
                        "props", toMap(entity)
                ));
            });
        }
    }

    /**
     * Obtém o próximo ID disponível da sequência de reservas de forma atômica.
     * @return O próximo ID único para uma reserva.
     */
    public Integer getNextId() {
        try (Session session = driver.session()) {
            return session.executeWrite(tx -> {
                String cypher = "MATCH (s:Sequence {name: 'reserva_id'}) " +
                        "SET s.current_id = s.current_id + 1 " +
                        "RETURN s.current_id AS nextId";
                Result result = tx.run(cypher);
                return result.single().get("nextId").asInt();
            });
        }
    }

    /**
     * Verifica se já existe uma reserva conflitante para uma dada sala e período.
     * Atualizado para usar LocalDateTime.
     */
    public boolean existeConflitoReserva(String codigoSala, LocalDateTime inicioReserva, LocalDateTime fimReserva) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (:Sala {codigo_sala: $codigoSala})<-[:FEITA_PARA]-(r:Reserva) " +
                        // Lógica de sobreposição de intervalos: (InicioA < FimB) AND (FimA > InicioB)
                        "WHERE r.inicio_reserva < $fimReserva AND r.fim_reserva > $inicioReserva " +
                        "RETURN count(r) > 0 AS conflito";

                Result result = tx.run(cypher, Values.parameters(
                        "codigoSala", codigoSala,
                        "inicioReserva", inicioReserva,
                        "fimReserva", fimReserva
                ));
                return result.single().get("conflito").asBoolean();
            });
        }
    }

    /**
     * Busca uma reserva pelo seu ID e retorna um objeto com detalhes completos,
     * navegando pelos relacionamentos para obter nomes da sala, professor e matéria.
     * @param idReserva O ID da reserva.
     * @return Um objeto ReservaDetalhada ou null se não for encontrada.
     */
    public ReservaDetalhada findReservaDetalhadaById(Integer idReserva) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (r:Reserva {id_reserva: $id}) " +
                        "OPTIONAL MATCH (r)-[:FEITA_PARA]->(s:Sala) " +
                        "OPTIONAL MATCH (r)-[:REALIZADA_POR]->(p:Professor) " +
                        "OPTIONAL MATCH (r)-[:REFERENTE_A]->(m:Materia) " +
                        "RETURN r, s.nome_sala AS nomeSala, p.nome_completo AS nomeProfessor, m.nome_materia AS nomeMateria";

                Result result = tx.run(cypher, Values.parameters("id", idReserva));
                if (result.hasNext()) {
                    return fromRecordToReservaDetalhada(result.single());
                }
                return null;
            });
        }
    }

    /**
     * Busca TODAS as reservas e retorna uma lista com detalhes completos.
     */
    public List<ReservaDetalhada> findAllDetalhado() {
        try (Session session = driver.session()) {
            String cypher = "MATCH (r:Reserva) " +
                    "OPTIONAL MATCH (r)-[:FEITA_PARA]->(s:Sala) " +
                    "OPTIONAL MATCH (r)-[:REALIZADA_POR]->(p:Professor) " +
                    "OPTIONAL MATCH (r)-[:REFERENTE_A]->(m:Materia) " +
                    "RETURN r, s.nome_sala AS nomeSala, p.nome_completo AS nomeProfessor, m.nome_materia AS nomeMateria " +
                    "ORDER BY r.id_reserva";
            return session.executeRead(tx -> tx.run(cypher).list(this::fromRecordToReservaDetalhada));
        }
    }

    // --- Métodos de Relatório ---

    /**
     * Busca todas as reservas em um determinado bloco, com detalhes.
     */
    public List<ReservaDetalhada> findAllReservasByBlocoDetalhado(String codigoBloco) {
        try (Session session = driver.session()) {
            String cypher = "MATCH (:Bloco {codigo_bloco: $codigo})<-[:LOCALIZADA_EM]-(:Sala)<-[:FEITA_PARA]-(r:Reserva) " +
                    "OPTIONAL MATCH (r)-[:FEITA_PARA]->(s:Sala) " +
                    "OPTIONAL MATCH (r)-[:REALIZADA_POR]->(p:Professor) " +
                    "OPTIONAL MATCH (r)-[:REFERENTE_A]->(m:Materia) " +
                    "RETURN r, s.nome_sala AS nomeSala, p.nome_completo AS nomeProfessor, m.nome_materia AS nomeMateria";
            return session.executeRead(tx -> tx.run(cypher, Values.parameters("codigo", codigoBloco)).list(this::fromRecordToReservaDetalhada));
        }
    }


    /**
     * Busca todas as reservas dentro de um período. O fim do período é opcional.
     * Se fimPeriodo for nulo, busca todas as reservas para o dia de inicioPeriodo.
     * @param inicioPeriodo O início do período de busca (obrigatório).
     * @param fimPeriodo O fim do período de busca (opcional).
     * @return Uma lista de objetos ReservaDetalhada.
     */
    public List<ReservaDetalhada> findAllReservasByPeriodoDetalhado(LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo) {
        try (Session session = driver.session()) {
            String cypher = "MATCH (r:Reserva) " +
                    "WHERE r.inicio_reserva >= $inicioPeriodo " +
                    "AND ($fimPeriodo IS NULL OR r.fim_reserva <= $fimPeriodo) " +
                    "OPTIONAL MATCH (r)-[:FEITA_PARA]->(s:Sala) " +
                    "OPTIONAL MATCH (r)-[:REALIZADA_POR]->(p:Professor) " +
                    "OPTIONAL MATCH (r)-[:REFERENTE_A]->(m:Materia) " +
                    "RETURN r, s.nome_sala AS nomeSala, p.nome_completo AS nomeProfessor, m.nome_materia AS nomeMateria " +
                    "ORDER BY r.inicio_reserva";

            // Se o fim do período não for fornecido, ajustamos o parâmetro para buscar apenas no dia do início.
            LocalDateTime fimPeriodoReal = (fimPeriodo == null)
                    ? inicioPeriodo.toLocalDate().atTime(LocalTime.MAX) // Fim do dia de inicioPeriodo
                    : fimPeriodo;

            return session.executeRead(tx -> tx.run(cypher, Values.parameters("inicioPeriodo", inicioPeriodo, "fimPeriodo", fimPeriodoReal))
                    .list(this::fromRecordToReservaDetalhada));
        }
    }

    // --- Métodos de Mapeamento e Helpers ---

    private ReservaDetalhada fromRecordToReservaDetalhada(Record record) {
        ReservaDetalhada detalhes = new ReservaDetalhada();
        setReservaFields(record.get("r").asNode(), detalhes);
        detalhes.setNomeSala(record.get("nomeSala").asString(null));
        detalhes.setNomeProfessor(record.get("nomeProfessor").asString(null));
        detalhes.setNomeMateria(record.get("nomeMateria").asString(null));
        return detalhes;
    }

    private void setReservaFields(Node node, Reserva reserva) {
        reserva.setIdReserva(node.get("id_reserva").asInt());
        // Usa a classe TypeConverter para a conversão segura
        reserva.setInicioReserva(toLocalDateTime(node.get("inicio_reserva")));
        reserva.setFimReserva(toLocalDateTime(node.get("fim_reserva")));
    }

    // --- Implementação dos Métodos Abstratos do GenericDAO ---

    @Override
    protected Reserva fromNode(Node node) {
        Reserva reserva = new Reserva();
        setReservaFields(node, reserva);
        return reserva;
    }

    @Override
    protected Map<String, Object> toMap(Reserva entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id_reserva", entity.getIdReserva());
        map.put("inicio_reserva", entity.getInicioReserva());
        map.put("fim_reserva", entity.getFimReserva());
        return map;
    }

    @Override
    protected String getLabel() { return "Reserva"; }

    @Override
    protected String getIdProperty() { return "id_reserva"; }

    @Override
    protected Integer getIdValue(Reserva entity) { return entity.getIdReserva(); }
}
