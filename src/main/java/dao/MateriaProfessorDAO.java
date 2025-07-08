package dao;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

import java.time.LocalDate;

public class MateriaProfessorDAO {

    private final Driver driver;

    public MateriaProfessorDAO(Driver driver) {
        this.driver = driver;
    }

    /**
     * Cria o relacionamento :LECIONA entre um Professor e uma Matéria.
     * @param matriculaProfessor Matrícula do professor.
     * @param codigoMateria Código da matéria.
     * @param inicioPeriodo Data de início do período.
     * @param fimPeriodo Data de fim do período.
     */
    public void createLecionaRelationship(Integer matriculaProfessor, String codigoMateria, LocalDate inicioPeriodo, LocalDate fimPeriodo) {
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                String cypher = "MATCH (p:Professor {matricula_professor: $matricula}), (m:Materia {codigo_materia: $codigo}) " +
                        "CREATE (p)-[r:LECIONA]->(m) " +
                        "SET r.inicio_periodo = $inicio, r.fim_periodo = $fim";
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
}
