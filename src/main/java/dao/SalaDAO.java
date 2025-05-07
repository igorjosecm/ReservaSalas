package dao;

import classes.CompositeKey;
import classes.Sala;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO extends GenericDAO<Sala> {
    public SalaDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected Sala fromResultSet(ResultSet rs) throws SQLException {
        Sala sala = new Sala();
        sala.setCodSala(rs.getString("cod_sala"));
        sala.setCodBloco(rs.getString("cod_bloco"));
        sala.setNomeSala(rs.getString("nome_sala"));
        sala.setAndar(rs.getInt("andar"));
        sala.setCapacidade(rs.getInt("capacidade"));
        return sala;
    }

    @Override
    protected Object[] getInsertValues(Sala entity) {
        return new Object[] {
                entity.getCodSala(),
                entity.getCodBloco(),
                entity.getNomeSala(),
                entity.getAndar(),
                entity.getCapacidade()
        };
    }

    @Override
    protected Object[] getUpdateValues(Sala entity) {
        return new Object[] {
                entity.getNomeSala(),
                entity.getAndar(),
                entity.getCapacidade()
        };
    }

    @Override
    protected String getAlias() {
        return "reserva_salas";
    }

    @Override
    protected List<String> getIdColumns() {
        List<String> idColumns = new ArrayList<>();
        idColumns.add("cod_sala");
        idColumns.add("cod_bloco");
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("nome_sala");
        columns.add("andar");
        columns.add("capacidade");
        return columns;
    }

    @Override
    protected String getTableName() {
        return "sala";
    }

    @Override
    protected CompositeKey getIdValues(Sala entity) {
        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("cod_sala", entity.getCodSala());
        compositeKey.addKey("cod_bloco", entity.getCodBloco());
        return compositeKey;
    }

    @Override
    protected void setGeneratedId(Sala entity, ResultSet generatedKeys) throws SQLException {}
}
