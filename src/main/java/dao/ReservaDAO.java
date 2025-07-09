package dao;

import classes.Reserva;
import classes.ReservaDetalhada;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaDAO extends GenericDAO<Reserva, Integer> {

    public ReservaDAO(Driver driver) {
        super(driver);
    }

    // Método create sobrescrito para lidar com os relacionamentos
    @Override
    public void create(Reserva entity) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                // 1. Cria o nó da Reserva com suas propriedades diretas
                String createNodeCypher = "CREATE (r:Reserva $props)";
                tx.run(createNodeCypher, Values.parameters("props", toMap(entity)));

                // 2. Cria o relacionamento com a Sala
                String linkSalaCypher = "MATCH (r:Reserva {id_reserva: $id}), (s:Sala {codigo_sala: $codigo}) " +
                        "CREATE (r)-[:FEITA_PARA]->(s)";
                tx.run(linkSalaCypher, Values.parameters("id", entity.getIdReserva(), "codigo", entity.getCodigoSala()));

                // 3. Cria o relacionamento com o Professor
                String linkProfessorCypher = "MATCH (r:Reserva {id_reserva: $id}), (p:Professor {matricula_professor: $matricula}) " +
                        "CREATE (r)-[:REALIZADA_POR]->(p)";
                tx.run(linkProfessorCypher, Values.parameters("id", entity.getIdReserva(), "matricula", entity.getMatriculaProfessor()));

                // 4. Cria o relacionamento com a Matéria
                String linkMateriaCypher = "MATCH (r:Reserva {id_reserva: $id}), (m:Materia {codigo_materia: $codigo}) " +
                        "CREATE (r)-[:REFERENTE_A]->(m)";
                tx.run(linkMateriaCypher, Values.parameters("id", entity.getIdReserva(), "codigo", entity.getCodigoMateria()));
            });
        }
    }

    /**
     * Verifica se já existe uma reserva conflitante para uma dada sala, data e hora.
     */
    public boolean existeConflitoReserva(String codigoSala, LocalDate dataInicio, LocalDate dataFim,
                                         LocalTime horaInicio, LocalTime horaFim) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (s:Sala {codigo_sala: $codigoSala})<-[:FEITA_PARA]-(r:Reserva) " +
                        "WHERE (r.data_inicio <= $dataFim AND r.data_fim >= $dataInicio) AND " +
                        "(r.hora_inicio < $horaFim AND r.hora_fim > $horaInicio) " +
                        "RETURN count(r) > 0 AS conflito";

                Result result = tx.run(cypher, Values.parameters(
                        "codigoSala", codigoSala,
                        "dataInicio", dataInicio,
                        "dataFim", dataFim,
                        "horaInicio", horaInicio,
                        "horaFim", horaFim
                ));
                return result.single().get("conflito").asBoolean();
            });
        }
    }

    /**
     * Busca todas as reservas em um determinado bloco.
     */
    public List<Reserva> findAllReservasByBloco(String codigoBloco) {
        List<Reserva> reservas = new ArrayList<>();
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (b:Bloco {codigo_bloco: $codigoBloco})<-[:LOCALIZADA_EM]-(s:Sala)<-[:FEITA_PARA]-(r:Reserva) " +
                        "RETURN r";
                Result result = tx.run(cypher, Values.parameters("codigoBloco", codigoBloco));
                while (result.hasNext()) {
                    reservas.add(fromNode(result.next().get("r").asNode()));
                }
                return reservas;
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
                    Record record = result.single();
                    return fromRecordToReservaDetalhada(record);
                }
                return null;
            });
        }
    }

    /**
     * Busca todas as reservas dentro de um período de data e hora específico.
     */
    public List<Reserva> findAllReservasByPeriodo(LocalDate dataInicio, LocalDate dataFim, LocalTime horaInicio, LocalTime horaFim) {
        List<Reserva> reservas = new ArrayList<>();
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (r:Reserva) " +
                        "WHERE r.data_inicio >= $dataInicio AND r.data_fim <= $dataFim AND " +
                        "r.hora_inicio >= $horaInicio AND r.hora_fim <= $horaFim " +
                        "RETURN r";
                Result result = tx.run(cypher, Values.parameters(
                        "dataInicio", dataInicio,
                        "dataFim", dataFim,
                        "horaInicio", horaInicio,
                        "horaFim", horaFim
                ));
                while (result.hasNext()) {
                    reservas.add(fromNode(result.next().get("r").asNode()));
                }
                return reservas;
            });
        }
    }

    /**
     * Método helper para converter um registro (Record) do Neo4j para o nosso DTO.
     */
    private ReservaDetalhada fromRecordToReservaDetalhada(Record record) {
        ReservaDetalhada detalhes = new ReservaDetalhada();
        Node reservaNode = record.get("r").asNode();

        // Popula os dados base da Reserva
        detalhes.setIdReserva(reservaNode.get("id_reserva").asInt());
        detalhes.setDataInicio(reservaNode.get("data_inicio").asLocalDate());
        detalhes.setDataFim(reservaNode.get("data_fim").asLocalDate());
        detalhes.setHoraInicio(reservaNode.get("hora_inicio").asLocalTime());
        detalhes.setHoraFim(reservaNode.get("hora_fim").asLocalTime());

        // Popula os dados dos relacionamentos
        detalhes.setNomeSala(record.get("nomeSala").asString(null)); // Usa null como default
        detalhes.setNomeProfessor(record.get("nomeProfessor").asString(null));
        detalhes.setNomeMateria(record.get("nomeMateria").asString(null));

        return detalhes;
    }

    // --- Implementação dos métodos abstratos ---

    @Override
    protected Reserva fromNode(Node node) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(node.get("id_reserva").asInt());
        reserva.setDataInicio(node.get("data_inicio").asLocalDate());
        reserva.setDataFim(node.get("data_fim").asLocalDate());
        reserva.setHoraInicio(node.get("hora_inicio").asLocalTime());
        reserva.setHoraFim(node.get("hora_fim").asLocalTime());
        // Os IDs (codigo_sala, etc.) não são propriedades do nó,
        // então eles não são mapeados aqui. Seriam obtidos por meio de consultas de relacionamento.
        return reserva;
    }

    @Override
    protected Map<String, Object> toMap(Reserva entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id_reserva", entity.getIdReserva());
        map.put("data_inicio", entity.getDataInicio());
        map.put("data_fim", entity.getDataFim());
        map.put("hora_inicio", entity.getHoraInicio());
        map.put("hora_fim", entity.getHoraFim());
        return map;
    }

    @Override
    protected String getLabel() {
        return "Reserva";
    }

    @Override
    protected String getIdProperty() {
        return "id_reserva";
    }

    @Override
    protected Integer getIdValue(Reserva entity) {
        return entity.getIdReserva();
    }
}
