package dao;

import classes.CompositeKey;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GenericDAO<T, K> {
    protected Connection connection;

    public GenericDAO(Connection connection) {
        this.connection = connection;
    }

    protected abstract T fromResultSet(ResultSet rs) throws SQLException;

    protected abstract Object[] getInsertValues(T entity);

    protected abstract Object[] getUpdateValues(T entity);

    protected abstract String getAlias();

    protected abstract List<String> getIdColumns();

    protected abstract List<String> getColumns();

    protected abstract String getTableName();

    protected abstract CompositeKey getIdValues(T entity);

    protected abstract String getGeneratedKey();

    protected String generateWhereClause() {
        List<String> idColumns = getIdColumns();
        StringBuilder where = new StringBuilder();
        for (int i = 0; i < idColumns.size(); i++) {
            if (i > 0) where.append(" AND ");
            where.append(idColumns.get(i)).append(" = ?");
        }
        return where.toString();
    }

    public void create(T entity) throws SQLException {
        String sql = generateInsertSQL();
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Object[] values = getInsertValues(entity);
            for (int i = 0; i < values.length; i++) {
                st.setObject(i + 1, values[i]);
            }
            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setGeneratedId(entity, generatedKeys);
                }
            }
        }
    }

    public T findById(K id) throws SQLException {
        String tableName = getAlias() + "." + getTableName();
        List<String> idColumns = getIdColumns();
        String sql = "SELECT * FROM " + tableName + " WHERE " + generateWhereClause();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (id.getClass().equals(CompositeKey.class)) {
                int i = 1;
                for (String idColumn : idColumns) {
                    stmt.setObject(i++, ((CompositeKey) id).getKey(idColumn));
                }
            } else {
                stmt.setObject(1, id);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                }
                return null;
            }
        }
    }

    public List<T> findAll() throws SQLException {
        String tableName = getAlias() + "." + getTableName();
        String sql = "SELECT * FROM " + tableName;
        List<T> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(fromResultSet(rs));
            }
        }

        return results;
    }


    public void update(T entity) throws SQLException {
        String tableName = getAlias() + "." + getTableName();
        List<String> idColumns = getIdColumns();
        String sql = "UPDATE " + tableName + " SET " + generateUpdateSetClause()
                + " WHERE " + generateWhereClause();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            Object[] updateValues = getUpdateValues(entity);
            int i = 1;
            for (Object value : updateValues) {
                stmt.setObject(i++, value);
            }

            CompositeKey id = getIdValues(entity);
            for (String idColumn : idColumns) {
                stmt.setObject(i++, id.getKey(idColumn));
            }

            stmt.executeUpdate();
        }
    }

    public void delete(K id) throws SQLException {
        String tableName = getAlias() + "." + getTableName();
        List<String> idColumns = getIdColumns();
        String sql = "DELETE FROM " + tableName + " WHERE " + generateWhereClause();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (id.getClass().equals(CompositeKey.class)) {
                int i = 1;
                for (String idColumn : idColumns) {
                    stmt.setObject(i++, ((CompositeKey) id).getKey(idColumn));
                }
            } else {
                stmt.setObject(1, id);
            }
            stmt.executeUpdate();
        }
    }

    protected String generateInsertSQL() {
        String tableName = getAlias() + "." + getTableName();
        List<String> columns = new ArrayList<>();
        String generatedKey = getGeneratedKey();

        if (generatedKey == null || generatedKey.isEmpty()) {
            columns.addAll(getIdColumns());
        }
        columns.addAll(getColumns());

        StringBuilder sql = new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(String.join(", ", columns))
                .append(") VALUES (")
                .append(String.join(", ", Collections.nCopies(columns.size(), "?")))
                .append(")");

        return sql.toString();
    }

    protected String generateUpdateSetClause() {
        List<String> columns = getColumns();

        StringBuilder setClause = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) setClause.append(", ");
            setClause.append(columns.get(i)).append(" = ?");
        }

        return setClause.toString();
    }

    protected abstract void setGeneratedId(T entity, ResultSet generatedKeys) throws SQLException;
}
