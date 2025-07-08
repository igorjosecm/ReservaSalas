package dao;

import classes.Bloco;
import org.neo4j.driver.Driver;
import org.neo4j.driver.types.Node;

import java.util.HashMap;
import java.util.Map;

public class BlocoDAO extends GenericDAO<Bloco, String> {

    public BlocoDAO(Driver driver) {
        super(driver);
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
