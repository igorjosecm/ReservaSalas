package dao;

import classes.CompositeKey;
import classes.Sala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO extends GenericDAO<Sala, String> {
    public SalaDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected Sala fromResultSet(ResultSet rs) throws SQLException {
        Sala sala = new Sala();
        sala.setCodigoSala(rs.getString("codigo_sala"));
        sala.setCodigoBloco(rs.getString("codigo_bloco"));
        sala.setNomeSala(rs.getString("nome_sala"));
        sala.setAndar(rs.getInt("andar"));
        sala.setCapacidade(rs.getInt("capacidade"));
        return sala;
    }

    @Override
    protected Object[] getInsertValues(Sala entity) {
        return new Object[] {
                entity.getCodigoSala(),
                entity.getCodigoBloco(),
                entity.getNomeSala(),
                entity.getAndar(),
                entity.getCapacidade()
        };
    }

    @Override
    protected Object[] getUpdateValues(Sala entity) {
        return new Object[] {
                entity.getCodigoBloco(),
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
        idColumns.add("codigo_sala");
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("codigo_bloco");
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
        CompositeKey key = new CompositeKey();
        key.addKey("codigo_sala", entity.getCodigoSala());
        return key;
    }

    @Override
    protected void setGeneratedId(Sala entity, ResultSet generatedKeys) throws SQLException {}

    public List<Sala> findSalasByBloco(String codigoBloco) throws SQLException  {
        String tableName = getAlias() + "." + getTableName();
        List<Sala> salas = new ArrayList<>();

        String sql = "SELECT * FROM " + tableName + " WHERE codigo_bloco = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoBloco);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    salas.add(fromResultSet(rs));
                }
            }
        }
        return salas;
    }
}
