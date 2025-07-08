package dao;

import classes.Sala;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaDAO extends GenericDAO<Sala, String> {

    public SalaDAO(Driver driver) {
        super(driver);
    }

    @Override
    protected Sala fromNode(Node node) {
        Sala sala = new Sala();
        // A propriedade codigo_bloco não existe mais no nó Sala
        // sala.setCodigoBloco(...);
        sala.setCodigoSala(node.get("codigo_sala").asString());
        sala.setNomeSala(node.get("nome_sala").asString());
        sala.setAndar(node.get("andar").asInt());
        sala.setCapacidade(node.get("capacidade").asInt());
        return sala;
    }

    @Override
    protected Map<String, Object> toMap(Sala entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("codigo_sala", entity.getCodigoSala());
        map.put("nome_sala", entity.getNomeSala());
        map.put("andar", entity.getAndar());
        map.put("capacidade", entity.getCapacidade());
        return map;
    }

    @Override
    protected String getLabel() {
        return "Sala";
    }

    @Override
    protected String getIdProperty() {
        return "codigo_sala";
    }

    @Override
    protected String getIdValue(Sala entity) {
        return entity.getCodigoSala();
    }

    /**
     * Encontra todas as salas que pertencem a um bloco específico.
     * @param codigoBloco O código do bloco a ser pesquisado.
     * @return Uma lista de salas.
     */
    public List<Sala> findSalasByBloco(String codigoBloco) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                // A query agora navega pelo relacionamento :LOCALIZADA_EM
                String cypher = "MATCH (s:Sala)-[:LOCALIZADA_EM]->(b:Bloco {codigo_bloco: $codigoBloco}) RETURN s";
                Result result = tx.run(cypher, Values.parameters("codigoBloco", codigoBloco));
                List<Sala> salas = new ArrayList<>();
                while (result.hasNext()) {
                    salas.add(fromNode(result.next().get("s").asNode()));
                }
                return salas;
            });
        }
    }
}
