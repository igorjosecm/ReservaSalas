package dao;

import classes.CompositeKey;
import classes.MateriaProfessor;
import classes.Professor;
import classes.Sala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaProfessorDAO extends GenericDAO<MateriaProfessor> {
    public MateriaProfessorDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected MateriaProfessor fromResultSet(ResultSet rs) throws SQLException {
        MateriaProfessor materiaProfessor = new MateriaProfessor();
        materiaProfessor.setCodigoMateria(rs.getString("codigo_materia"));
        materiaProfessor.setMatriculaProfessor(rs.getInt("matricula_professor"));
        materiaProfessor.setInicioPeriodo(rs.getDate("inicio_periodo").toLocalDate());
        materiaProfessor.setFimPeriodo(rs.getDate("fim_periodo").toLocalDate());
        return materiaProfessor;
    }

    @Override
    protected Object[] getInsertValues(MateriaProfessor entity) {
        return new Object[] {
                entity.getCodigoMateria(),
                entity.getMatriculaProfessor(),
                entity.getInicioPeriodo(),
                entity.getFimPeriodo(),
        };
    }

    @Override
    protected Object[] getUpdateValues(MateriaProfessor entity) {
        return new Object[] {
                entity.getInicioPeriodo(),
                entity.getFimPeriodo(),
        };
    }

    @Override
    protected String getAlias() {
        return "reserva_salas";
    }

    @Override
    protected List<String> getIdColumns() {
        List<String> idColumns = new ArrayList<>();
        idColumns.add("codigo_materia");
        idColumns.add("matricula_professor");
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("inicio_periodo");
        columns.add("fim_periodo");
        return columns;
    }

    @Override
    protected String getTableName() {
        return "materia_professor";
    }

    @Override
    protected CompositeKey getIdValues(MateriaProfessor entity) {
        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("codigo_materia", entity.getCodigoMateria());
        compositeKey.addKey("matricula_professor", entity.getMatriculaProfessor());
        return compositeKey;
    }

    @Override
    protected void setGeneratedId(MateriaProfessor entity, ResultSet generatedKeys) throws SQLException {}

    public List<MateriaProfessor> findAllMateriasOfProfessor(Integer matriculaProfessor) throws SQLException  {
        String tableName = getAlias() + "." + getTableName();
        List<MateriaProfessor> materiasProfessor = new ArrayList<>();

        String sql = "SELECT * FROM " + tableName + " WHERE matricula_professor = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, matriculaProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    materiasProfessor.add(fromResultSet(rs));
                }
            }
        }
        return materiasProfessor;
    }
}
