package dao;

import classes.CompositeKey;
import classes.Reserva;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO extends GenericDAO<Reserva> {
    public ReservaDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected Reserva fromResultSet(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(rs.getInt("id_reserva"));
        reserva.setCodigoSala(rs.getString("codigo_sala"));
        reserva.setMatriculaProfessor(rs.getInt("matricula_professor"));
        reserva.setCodigoMateria(rs.getString("codigo_materia"));
        reserva.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        reserva.setDataFim(rs.getDate("data_fim").toLocalDate());
        reserva.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        reserva.setHoraFim(rs.getTime("hora_fim").toLocalTime());
        return reserva;
    }

    @Override
    protected Object[] getInsertValues(Reserva entity) {
        return new Object[] {
                entity.getIdReserva(),
                entity.getCodigoSala(),
                entity.getMatriculaProfessor(),
                entity.getCodigoMateria(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getHoraInicio(),
                entity.getHoraFim(),
        };
    }

    @Override
    protected Object[] getUpdateValues(Reserva entity) {
        return new Object[] {
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getHoraInicio(),
                entity.getHoraFim(),
        };
    }

    @Override
    protected String getAlias() {
        return "reserva_salas";
    }

    @Override
    protected List<String> getIdColumns() {
        List<String> idColumns = new ArrayList<>();
        idColumns.add("id_reserva");
        idColumns.add("codigo_sala");
        idColumns.add("matricula_professor");
        idColumns.add("codigo_materia");
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        columns.add("id_reserva");
        columns.add("codigo_sala");
        columns.add("matricula_professor");
        columns.add("codigo_materia");
        columns.add("data_inicio");
        columns.add("data_fim");
        columns.add("hora_inicio");
        columns.add("hora_fim");
        return columns;
    }

    @Override
    protected String getTableName() {
        return "reserva";
    }

    @Override
    protected CompositeKey getIdValues(Reserva entity) {
        CompositeKey compositeKey = new CompositeKey();
        compositeKey.addKey("id_reserva", entity.getIdReserva());
        compositeKey.addKey("codigo_sala", entity.getCodigoSala());
        compositeKey.addKey("matricula_professor", entity.getMatriculaProfessor());
        compositeKey.addKey("codigo_materia", entity.getCodigoMateria());
        return compositeKey;
    }

    @Override
    protected void setGeneratedId(Reserva entity, ResultSet generatedKeys) throws SQLException {
        entity.setIdReserva(generatedKeys.getInt("id_reserva"));
    }
}
