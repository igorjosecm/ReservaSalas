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

    /**
     * Cria um nó de Sala e, na mesma transação, o conecta a um Bloco existente.
     * @param sala O objeto Sala a ser criado.
     * @param codigoBloco O código do Bloco ao qual a sala será vinculada.
     */
    public void createAndLinkToBloco(Sala sala, String codigoBloco) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                // Query combinada para criar o nó e o relacionamento
                String cypher = "MATCH (b:Bloco {codigo_bloco: $codigoBloco}) " +
                        "CREATE (s:Sala $props) " +
                        "CREATE (s)-[:LOCALIZADA_EM]->(b)";

                tx.run(cypher, Values.parameters(
                        "codigoBloco", codigoBloco,
                        "props", toMap(sala)
                ));
            });
        }
    }

    /**
     * Encontra todas as salas que pertencem a um bloco específico.
     * @param codigoBloco O código do bloco a ser pesquisado.
     * @return Uma lista de salas.
     */
    public List<Sala> findSalasByBloco(String codigoBloco) {
        try (Session session = driver.session()) {
            String cypher = "MATCH (s:Sala)-[:LOCALIZADA_EM]->(b:Bloco {codigo_bloco: $codigoBloco}) " +
                    "WHERE s.ativo = true " +
                    "RETURN s";
            return session.executeRead(tx -> tx.run(cypher, Values.parameters("codigoBloco", codigoBloco))
                    .list(record -> fromNode(record.get("s").asNode())));
        }
    }

    @Override
    public List<Sala> findAll() {
        try (Session session = driver.session()) {
            String cypher = "MATCH (n:Sala) WHERE n.ativo = true RETURN n";
            return session.executeRead(tx -> tx.run(cypher).list(record -> fromNode(record.get("n").asNode())));
        }
    }

    public void deactivateSala(String codigoSala) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (s:Sala {codigo_sala: $id}) SET s.ativo = false";
                tx.run(cypher, Values.parameters("id", codigoSala));
            });
        }
    }

    // --- Implementação dos métodos abstratos do GenericDAO ---

    @Override
    protected Sala fromNode(Node node) {
        Sala sala = new Sala();
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
        map.put("ativo", true);
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
}
