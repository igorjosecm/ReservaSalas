package dao;

import classes.Materia;
import org.neo4j.driver.Driver;
import org.neo4j.driver.types.Node;

import java.util.HashMap;
import java.util.Map;

public class MateriaDAO extends GenericDAO<Materia, String> {

    public MateriaDAO(Driver driver) {
        super(driver);
    }

    @Override
    protected Materia fromNode(Node node) {
        Materia materia = new Materia();
        materia.setCodigoMateria(node.get("codigo_materia").asString());
        materia.setNomeMateria(node.get("nome_materia").asString());
        materia.setCargaHoraria(node.get("carga_horaria").asInt());
        return materia;
    }

    @Override
    protected Map<String, Object> toMap(Materia entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("codigo_materia", entity.getCodigoMateria());
        map.put("nome_materia", entity.getNomeMateria());
        map.put("carga_horaria", entity.getCargaHoraria());
        return map;
    }

    @Override
    protected String getLabel() {
        return "Materia";
    }

    @Override
    protected String getIdProperty() {
        return "codigo_materia";
    }

    @Override
    protected String getIdValue(Materia entity) {
        return entity.getCodigoMateria();
    }
}
