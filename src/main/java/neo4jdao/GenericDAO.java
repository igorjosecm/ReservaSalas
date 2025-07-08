package neo4jdao;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO genérico para operações CRUD em nós no Neo4j.
 * @param <T> O tipo da entidade (ex: Bloco, Sala).
 * @param <K> O tipo do identificador de negócio (ex: String para codigo_bloco, Integer para matricula_professor).
 */
public abstract class GenericDAO<T, K> {

    protected final Driver driver;

    public GenericDAO(Driver driver) {
        this.driver = driver;
    }

    // MÉTODOS ABSTRATOS A SEREM IMPLEMENTADOS PELAS CLASSES FILHAS

    /**
     * Mapeia um nó retornado pelo Neo4j para um objeto da entidade Java.
     * @param node O nó do Neo4j.
     * @return A entidade Java populada.
     */
    protected abstract T fromNode(Node node);

    /**
     * Converte uma entidade Java em um mapa de propriedades para ser usado em queries Cypher.
     * @param entity A entidade a ser convertida.
     * @return Um mapa onde a chave é o nome da propriedade e o valor é o valor da propriedade.
     */
    protected abstract Map<String, Object> toMap(T entity);

    /**
     * Retorna a Label do nó no banco de dados (ex: "Bloco", "Sala").
     * @return A string da Label.
     */
    protected abstract String getLabel();

    /**
     * Retorna o nome da propriedade que serve como identificador de negócio (ex: "codigo_bloco").
     * @return A string do nome da propriedade.
     */
    protected abstract String getIdProperty();

    /**
     * Obtém o valor do identificador de negócio a partir de uma entidade.
     * @param entity A entidade.
     * @return O valor do ID.
     */
    protected abstract K getIdValue(T entity);


    // MÉTODOS CRUD CONCRETOS

    public void create(T entity) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                String cypher = String.format("CREATE (n:%s $props)", getLabel());
                tx.run(cypher, Values.parameters("props", toMap(entity)));
                return null;
            });
        }
    }

    public T findById(K id) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = String.format("MATCH (n:%s {%s: $id}) RETURN n", getLabel(), getIdProperty());
                Result result = tx.run(cypher, Values.parameters("id", id));
                if (result.hasNext()) {
                    return fromNode(result.single().get("n").asNode());
                }
                return null;
            });
        }
    }

    public List<T> findAll() {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = String.format("MATCH (n:%s) RETURN n", getLabel());
                Result result = tx.run(cypher);
                List<T> entities = new ArrayList<>();
                while (result.hasNext()) {
                    entities.add(fromNode(result.next().get("n").asNode()));
                }
                return entities;
            });
        }
    }

    public void update(T entity) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                String cypher = String.format("MATCH (n:%s {%s: $id}) SET n += $props", getLabel(), getIdProperty());
                tx.run(cypher, Values.parameters(
                        "id", getIdValue(entity),
                        "props", toMap(entity)
                ));
                return null;
            });
        }
    }

    public void delete(K id) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                // DETACH DELETE remove o nó e todos os relacionamentos conectados a ele.
                String cypher = String.format("MATCH (n:%s {%s: $id}) DETACH DELETE n", getLabel(), getIdProperty());
                tx.run(cypher, Values.parameters("id", id));
                return null;
            });
        }
    }
}