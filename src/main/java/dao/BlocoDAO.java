package dao;

import classes.Bloco;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import java.util.HashMap;
import java.util.Map;

public class BlocoDAO extends GenericDAO<Bloco, String> {

    public BlocoDAO(Driver driver) {
        super(driver);
    }

    /**
     * Encontra o Bloco associado a uma Sala específica.
     * @param codigoSala O código da sala.
     * @return O objeto Bloco correspondente, ou null se não for encontrado.
     */
    public Bloco findBlocoBySala(String codigoSala) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (:Sala {codigo_sala: $codigoSala})-[:LOCALIZADA_EM]->(b:Bloco) RETURN b";
                Result result = tx.run(cypher, Values.parameters("codigoSala", codigoSala));
                if (result.hasNext()) {
                    return fromNode(result.single().get("b").asNode());
                }
                return null;
            });
        }
    }

    @Override
    protected Bloco fromNode(Node node) {
        Bloco bloco = new Bloco();
        bloco.setCodigoBloco(node.get("codigo_bloco").asString());
        bloco.setNomeBloco(node.get("nome_bloco").asString());
        bloco.setNumAndares(node.get("num_andares").asInt());
        return bloco;
    }

    @Override
    protected Map<String, Object> toMap(Bloco entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("codigo_bloco", entity.getCodigoBloco());
        map.put("nome_bloco", entity.getNomeBloco());
        map.put("num_andares", entity.getNumAndares());
        return map;
    }

    @Override
    protected String getLabel() {
        return "Bloco";
    }

    @Override
    protected String getIdProperty() {
        return "codigo_bloco";
    }

    @Override
    protected String getIdValue(Bloco entity) {
        return entity.getCodigoBloco();
    }
}
