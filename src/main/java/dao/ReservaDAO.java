package dao;

import classes.Reserva;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDAO extends GenericDAO<Reserva, Integer> {

    public ReservaDAO(Driver driver) {
        super(driver);
    }

    // Sobrescrevemos o método create para lidar com os relacionamentos
    @Override
    public void create(Reserva entity) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                // 1. Cria o nó da Reserva
                String createNodeCypher = String.format("CREATE (n:%s $props)", getLabel());
                tx.run(createNodeCypher, Values.parameters("props", toMap(entity)));

                // 2. Cria os relacionamentos
                String linkSalaCypher = "MATCH (r:Reserva {id_reserva: $id}), (s:Sala {codigo_sala: $codigo}) CREATE (r)-[:FEITA_PARA]->(s)";
                tx.run(linkSalaCypher, Values.parameters("id", entity.getIdReserva(), "codigo", entity.getCodigoSala()));

                String linkProfessorCypher = "MATCH (r:Reserva {id_reserva: $id}), (p:Professor {matricula_professor: $matricula}) CREATE (r)-[:REALIZADA_POR]->(p)";
                tx.run(linkProfessorCypher, Values.parameters("id", entity.getIdReserva(), "matricula", entity.getMatriculaProfessor()));

                String linkMateriaCypher = "MATCH (r:Reserva {id_reserva: $id}), (m:Materia {codigo_materia: $codigo}) CREATE (r)-[:REFERENTE_A]->(m)";
                tx.run(linkMateriaCypher, Values.parameters("id", entity.getIdReserva(), "codigo", entity.getCodigoMateria()));
            });
        }
    }

    public boolean existeConflitoReserva(String codigoSala, LocalDate dataInicio, LocalDate dataFim,
                                         LocalTime horaInicio, LocalTime horaFim) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                // A query busca por uma reserva na mesma sala que tenha sobreposição de data e hora.
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


    @Override
    protected Reserva fromNode(Node node) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(node.get("id_reserva").asInt());
        reserva.setDataInicio(node.get("data_inicio").asLocalDate());
        reserva.setDataFim(node.get("data_fim").asLocalDate());
        reserva.setHoraInicio(node.get("hora_inicio").asLocalTime());
        reserva.setHoraFim(node.get("hora_fim").asLocalTime());
        // As outras propriedades (codigo_sala, etc.) agora são obtidas via relacionamentos.
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
