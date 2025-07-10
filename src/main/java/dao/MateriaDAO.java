package dao;

import classes.Materia;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MateriaDAO extends GenericDAO<Materia, String> {

    public MateriaDAO(Driver driver) {
        super(driver);
    }

    /**
     * Modifica o findAll para retornar apenas matérias ativas por padrão.
     */
    @Override
    public List<Materia> findAll() {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (n:Materia) WHERE n.ativo = true RETURN n";
                Result result = tx.run(cypher);
                List<Materia> entities = new ArrayList<>();
                while (result.hasNext()) {
                    entities.add(fromNode(result.next().get("n").asNode()));
                }
                return entities;
            });
        }
    }

    /**
     * Desativa uma matéria marcando sua propriedade 'ativo' como false.
     * Isso preserva o nó e todo o seu histórico de relacionamentos.
     * @param codigoMateria O código da matéria a ser desativada.
     */
    public void deactivateMateria(String codigoMateria) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (m:Materia {codigo_materia: $id}) SET m.ativo = false";
                tx.run(cypher, Values.parameters("id", codigoMateria));
            });
        }
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
        map.put("ativo", true);
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
