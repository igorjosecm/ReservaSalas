package dao;

import classes.Materia;
import classes.MateriaLecionada;
import classes.MateriaProfessorRelacao;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

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

    /**
     * Relatório: Encontra todas as matérias que um professor leciona, incluindo o período.
     * @param matriculaProfessor A matrícula do professor.
     * @return Uma lista de objetos MateriaLecionada.
     */
    public List<MateriaLecionada> findMateriasLecionadasPorProfessor(Integer matriculaProfessor) {
        List<MateriaLecionada> materiasLecionadas = new ArrayList<>();

        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (:Professor {matricula_professor: $matricula})-[r:LECIONA]->(m:Materia) RETURN m, r";
                Result result = tx.run(cypher, Values.parameters("matricula", matriculaProfessor));

                while(result.hasNext()) {
                    Record record = result.next();
                    Node materiaNode = record.get("m").asNode();
                    Relationship rel = record.get("r").asRelationship();

                    MateriaLecionada dto = new MateriaLecionada();
                    dto.setCodigoMateria(materiaNode.get("codigo_materia").asString());
                    dto.setNomeMateria(materiaNode.get("nome_materia").asString());
                    dto.setCargaHoraria(materiaNode.get("carga_horaria").asInt());
                    dto.setInicioPeriodo(rel.get("inicio_periodo").asLocalDate());
                    dto.setFimPeriodo(rel.get("fim_periodo").asLocalDate());

                    materiasLecionadas.add(dto);
                }
                return materiasLecionadas;
            });
        }
    }

    /**
     * Busca todas as relações :LECIONA existentes no banco de dados.
     * @return Uma lista de DTOs contendo os detalhes de cada relação.
     */
    public List<MateriaProfessorRelacao> findAllLecionaRelationships() {
        List<MateriaProfessorRelacao> relacoes = new ArrayList<>();
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                String cypher = "MATCH (p:Professor)-[r:LECIONA]->(m:Materia) " +
                        "RETURN p.matricula_professor AS matricula, p.nome_completo AS nomeProf, " +
                        "m.codigo_materia AS codigo, m.nome_materia AS nomeMat, " +
                        "r.inicio_periodo AS inicio, r.fim_periodo AS fim " +
                        "ORDER BY p.nome_completo, m.nome_materia";

                Result result = tx.run(cypher);
                while(result.hasNext()) {
                    Record record = result.next();
                    MateriaProfessorRelacao dto = new MateriaProfessorRelacao();

                    dto.setMatriculaProfessor(record.get("matricula").asInt());
                    dto.setNomeProfessor(record.get("nomeProf").asString());
                    dto.setCodigoMateria(record.get("codigo").asString());
                    dto.setNomeMateria(record.get("nomeMat").asString());
                    dto.setInicioPeriodo(record.get("inicio").asLocalDate());
                    dto.setFimPeriodo(record.get("fim").asLocalDate());

                    relacoes.add(dto);
                }
                return relacoes;
            });
        }
    }

}
