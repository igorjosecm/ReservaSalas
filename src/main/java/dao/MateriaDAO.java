package dao;

import classes.CompositeKey;
import classes.Materia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO extends GenericDAO<Materia> {
    public MateriaDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected Materia fromResultSet(ResultSet rs) throws SQLException {
        Materia materia = new Materia();
        materia.setCodigoMateria(rs.getString("codigo_materia"));
        materia.setNomeMateria(rs.getString("nome_materia"));
        materia.setCargaHoraria(rs.getInt("carga_horaria"));
        return materia;
    }

    @Override
    protected Object[] getInsertValues(Materia entity) {
        return new Object[] {
                entity.getCodigoMateria(),
                entity.getNomeMateria(),
                entity.getCargaHoraria(),
        };
    }

    @Override
    protected Object[] getUpdateValues(Materia entity) {
        return new Object[] {
                entity.getNomeMateria(),
                entity.getCargaHoraria(),
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
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("codigo_materia");
        columns.add("nome_materia");
        columns.add("carga_horaria");
        return columns;
    }

    @Override
    protected String getTableName() {
        return "materia";
    }

    @Override
    protected CompositeKey getIdValues(Materia entity) {
        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("codigo_materia", entity.getCodigoMateria());
        return compositeKey;
    }

    @Override
    protected void setGeneratedId(Materia entity, ResultSet generatedKeys) throws SQLException {}
}
