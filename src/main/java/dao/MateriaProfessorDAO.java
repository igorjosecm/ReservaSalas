package dao;

import classes.Materia;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.Record;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MateriaProfessorDAO {

    private final Driver driver;

    public MateriaProfessorDAO(Driver driver) {
        this.driver = driver;
    }

    /**
     * Cria o relacionamento :LECIONA entre um Professor e uma Matéria,
     * com propriedades que definem o período.
     * @param matriculaProfessor Matrícula do professor.
     * @param codigoMateria Código da matéria.
     * @param inicioPeriodo Data de início do período.
     * @param fimPeriodo Data de fim do período.
     */
    public void createLecionaRelationship(Integer matriculaProfessor, String codigoMateria, LocalDate inicioPeriodo, LocalDate fimPeriodo) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (p:Professor {matricula_professor: $matricula}), (m:Materia {codigo_materia: $codigo}) " +
                        "MERGE (p)-[r:LECIONA]->(m) " + // MERGE evita criar relacionamentos duplicados
                        "ON CREATE SET r.inicio_periodo = $inicio, r.fim_periodo = $fim " +
                        "ON MATCH SET r.inicio_periodo = $inicio, r.fim_periodo = $fim"; // Atualiza se já existir

                tx.run(cypher, Values.parameters(
                        "matricula", matriculaProfessor,
                        "codigo", codigoMateria,
                        "inicio", inicioPeriodo,
                        "fim", fimPeriodo
                ));
            });
        }
    }

    /**
     * Remove o relacionamento :LECIONA entre um Professor e uma Matéria.
     * @param matriculaProfessor Matrícula do professor.
     * @param codigoMateria Código da matéria.
     */
    public void deleteLecionaRelationship(Integer matriculaProfessor, String codigoMateria) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (p:Professor {matricula_professor: $matricula})-[r:LECIONA]->(m:Materia {codigo_materia: $codigo}) DELETE r";
                tx.run(cypher, Values.parameters(
                        "matricula", matriculaProfessor,
                        "codigo", codigoMateria
                ));
            });
        }
    }

    /**
     * Relatório: Encontra todas as matérias que um determinado professor leciona.
     * @param matriculaProfessor A matrícula do professor.
     * @return Uma lista de objetos Materia.
     */
    public List<Materia> findMateriasOfProfessor(Integer matriculaProfessor) {
        List<Materia> materias = new ArrayList<>();
        MateriaDAO materiaDAO = new MateriaDAO(driver);

        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (:Professor {matricula_professor: $matricula})-[:LECIONA]->(m:Materia) RETURN m";
                Result result = tx.run(cypher, Values.parameters("matricula", matriculaProfessor));

                while(result.hasNext()) {
                    Record record = result.next();
                    materias.add(materiaDAO.fromNode(record.get("m").asNode()));
                }
                return materias;
            });
        }
    }
}
