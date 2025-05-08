package dao;

import classes.CompositeKey;
import classes.Professor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO extends GenericDAO<Professor, Integer> {
    public ProfessorDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected Professor fromResultSet(ResultSet rs) throws SQLException {
        Professor professor = new Professor();
        professor.setMatriculaProfessor(rs.getInt("matricula_professor"));
        professor.setNomeCompleto(rs.getString("nome_completo"));
        professor.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        professor.setEmail(rs.getString("email"));
        return professor;
    }

    @Override
    protected Object[] getInsertValues(Professor entity) {
        return new Object[] {
                entity.getMatriculaProfessor(),
                entity.getNomeCompleto(),
                entity.getDataNascimento(),
                entity.getEmail(),
        };
    }

    @Override
    protected Object[] getUpdateValues(Professor entity) {
        return new Object[] {
                entity.getNomeCompleto(),
                entity.getDataNascimento(),
                entity.getEmail(),
        };
    }

    @Override
    protected String getAlias() {
        return "reserva_salas";
    }

    @Override
    protected List<String> getIdColumns() {
        List<String> idColumns = new ArrayList<>();
        idColumns.add("matricula_professor");
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("nome_completo");
        columns.add("data_nascimento");
        columns.add("email");
        return columns;
    }

    @Override
    protected String getTableName() {
        return "professor";
    }

    @Override
    protected CompositeKey getIdValues(Professor entity) {
        CompositeKey key = new CompositeKey();
        key.addKey("matricula_professor", entity.getMatriculaProfessor());
        return key;
    }

    @Override
    protected void setGeneratedId(Professor entity, ResultSet generatedKeys) throws SQLException {}
}
