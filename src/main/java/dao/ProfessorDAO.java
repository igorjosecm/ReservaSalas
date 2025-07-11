package dao;

import classes.Professor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorDAO extends GenericDAO<Professor, Integer> {

    public ProfessorDAO(Driver driver) {
        super(driver);
    }

    /**
     * Modifica o findAll para retornar apenas professores ativos por padrão.
     */
    @Override
    public List<Professor> findAll() {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (n:Professor) WHERE n.ativo = true RETURN n";
                Result result = tx.run(cypher);
                List<Professor> entities = new ArrayList<>();
                while (result.hasNext()) {
                    entities.add(fromNode(result.next().get("n").asNode()));
                }
                return entities;
            });
        }
    }

    /**
     * Desativa um professor marcando sua propriedade 'ativo' como false.
     * Isso preserva o nó e todo o seu histórico de relacionamentos.
     * @param matriculaProfessor A matrícula do professor a ser desativado.
     */
    public void deactivateProfessor(Integer matriculaProfessor) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (p:Professor {matricula_professor: $id}) SET p.ativo = false";
                tx.run(cypher, Values.parameters("id", matriculaProfessor));
            });
        }
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
        map.put("ativo", true);
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
