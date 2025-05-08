package dao;

import classes.CompositeKey;
import classes.Reserva;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO extends GenericDAO<Reserva, Integer> {
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
        return idColumns;
    }

    @Override
    protected List<String> getColumns() {
        List<String> columns = new ArrayList<>();
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
        CompositeKey key = new CompositeKey();
        key.addKey("id_reserva", entity.getIdReserva());
        return key;
    }

    @Override
    protected String getGeneratedKey() {
        return "id_reserva";
    }

    @Override
    protected void setGeneratedId(Reserva entity, ResultSet generatedKeys) throws SQLException {
        entity.setIdReserva(generatedKeys.getInt("id_reserva"));
    }

    public boolean existeConflitoReserva(String codigoSala, LocalDate dataInicio, LocalDate dataFim,
                                         LocalTime horaInicio, LocalTime horaFim) throws SQLException  {
        String tableName = getAlias() + "." + getTableName();

        boolean existeConflitoReserva = false;

        String sql = " SELECT 1 FROM " + tableName + " WHERE codigo_sala = ? AND data_inicio <= ? AND data_fim >= ? AND hora_inicio < ? AND hora_fim > ? LIMIT 1 ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoSala);
            stmt.setDate(2, Date.valueOf(dataInicio));
            stmt.setDate(3, Date.valueOf(dataFim));
            stmt.setTime(4, Time.valueOf(horaInicio));
            stmt.setTime(5, Time.valueOf(horaFim));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    existeConflitoReserva = true;
                }
            }
        }
        return existeConflitoReserva;
    }

    public List<Reserva> findAllReservasByPeriodo(LocalDate dataInicio, LocalDate dataFim,
                                         LocalTime horaInicio, LocalTime horaFim) throws SQLException  {
        String tableName = getAlias() + "." + getTableName();

        List<Reserva> reservas = new ArrayList<>();

        String sql = " SELECT * FROM " + tableName + " WHERE id_reserva > 0 ";
        if (dataInicio != null) {
            sql += " AND data_inicio > ?1 ";
        }
        if (dataFim != null) {
            sql += " AND data_fim < ?2 ";
        }
        if (horaInicio != null) {
            sql += " AND hora_inicio => ?3 ";
        }
        if (horaFim != null) {
            sql += " AND hora_fim <= ?4 ";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (dataInicio != null) {
                stmt.setDate(1, Date.valueOf(dataInicio.minusDays(1)));
            }
            if (dataFim != null) {
                stmt.setDate(2, Date.valueOf(dataFim.plusDays(1)));
            }
            if (horaInicio != null) {
                stmt.setTime(3, Time.valueOf(horaInicio));
            }
            if (horaFim != null) {
                stmt.setTime(4, Time.valueOf(horaFim));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    reservas.add(fromResultSet(rs));
                }
            }
        }
        return reservas;
    }

    public List<Reserva> findAllReservasByBloco(String codigoBloco) throws SQLException  {
        List<Reserva> reservas = new ArrayList<>();

        String sql =
                " SELECT reserva.id_reserva, reserva.codigo_sala, reserva.matricula_professor, reserva.codigo_materia," +
                        " reserva.data_inicio, reserva.data_fim, reserva.hora_inicio, reserva.hora_fim" +
                        " FROM reserva_salas.reserva join reserva_salas.sala" +
                        " on (sala.codigo_sala = " + getTableName() + ".codigo_sala) WHERE sala.codigo_bloco = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoBloco);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    reservas.add(fromResultSet(rs));
                }
            }
        }
        return reservas;
    }
}
