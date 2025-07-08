package dao;

import classes.Professor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.types.Node;

import java.util.HashMap;
import java.util.Map;

public class ProfessorDAO extends GenericDAO<Professor, Integer> {

    public ProfessorDAO(Driver driver) {
        super(driver);
    }

    @Override
    protected Professor fromNode(Node node) {
        Professor professor = new Professor();
        professor.setMatriculaProfessor(node.get("matricula_professor").asInt());
        professor.setNomeCompleto(node.get("nome_completo").asString());
        professor.setEmail(node.get("email").asString());
        if (node.containsKey("data_nascimento")) {
            professor.setDataNascimento(node.get("data_nascimento").asLocalDate());
        }
        return professor;
    }

    @Override
    protected Map<String, Object> toMap(Professor entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("matricula_professor", entity.getMatriculaProfessor());
        map.put("nome_completo", entity.getNomeCompleto());
        map.put("email", entity.getEmail());
        map.put("data_nascimento", entity.getDataNascimento());
        return map;
    }

    @Override
    protected String getLabel() {
        return "Professor";
    }

    @Override
    protected String getIdProperty() {
        return "matricula_professor";
    }

    @Override
    protected Integer getIdValue(Professor entity) {
        return entity.getMatriculaProfessor();
    }
}
