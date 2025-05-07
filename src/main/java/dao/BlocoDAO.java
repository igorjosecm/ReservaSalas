package dao;

import classes.Bloco;
import classes.CompositeKey;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlocoDAO extends GenericDAO<Bloco> {
    public BlocoDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected Bloco fromResultSet(ResultSet rs) throws SQLException {
        Bloco bloco = new Bloco();
        bloco.setCodigoBloco(rs.getString("codigo_bloco"));
        bloco.setNomeBloco(rs.getString("nome_bloco"));
        bloco.setNumAndares(rs.getInt("num_andares"));
        return bloco;
    }

    @Override
    protected Object[] getInsertValues(Bloco entity) {
        return new Object[] {
                entity.getCodigoBloco(),
                entity.getNomeBloco(),
                entity.getNumAndares(),
        };
    }

    @Override
    protected Object[] getUpdateValues(Bloco entity) {
        return new Object[] {
                entity.getNomeBloco(),
                entity.getNumAndares(),
        };
    }

    @Override
    protected String getAlias() {
        return "reserva_salas";
    }

    @Override
    protected List<String> getIdColumns() {
        List<String> idColumns = new ArrayList<>();
        idColumns.add("codigo_bloco");
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("codigo_bloco");
        columns.add("nome_bloco");
        columns.add("num_andares");
        return columns;
    }

    @Override
    protected String getTableName() {
        return "bloco";
    }

    @Override
    protected CompositeKey getIdValues(Bloco entity) {
        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("codigo_bloco", entity.getCodigoBloco());
        return compositeKey;
    }

    @Override
    protected void setGeneratedId(Bloco entity, ResultSet generatedKeys) throws SQLException {}
}
