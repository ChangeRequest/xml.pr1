package com.github.changerequest.store.h2storage;

import com.github.changerequest.store.h2storage.mapper.RowMapper;
import com.github.changerequest.store.persistenceapi.StoredEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * This is the central class for working with JDBC. It simplifies the use of JDBC and helps to avoid common errors.
 * It executes core JDBC workflow, leaving application code to provide SQL and extract results. This class executes SQL
 * queries or updates, initiating iteration over ResultSets and catching JDBC exceptions.
 */
public class JdbcTemplate {

    /**
     * Execute select query for one entity.
     *
     * @param sql        - sql to be executed
     * @param params     - sql parameters
     * @param mapper     - mapper to map single entity
     * @param connection - connection which should be used
     * @return result entity
     */
    public <T extends StoredEntity<Long>> T queryForObject(String sql, Object[] params, RowMapper<T> mapper, Connection connection) {
        return processRequest(sql, params, connection, (resultSet) -> {
            T result = null;
            if (resultSet.next()) {
                result = mapper.mapRow(resultSet);
            }
            return result;
        });
    }

    /**
     * Execute select query which should get multiple rows.
     *
     * @param sql    - sql to be executed
     * @param params - sql parameters
     * @param mapper - mapper to map single entity
     * @return result list of entities
     */
    public <T extends StoredEntity<Long>> List<T> queryForList(final String sql, final Object[] params, final RowMapper<T> mapper, Connection connection) {
        return processRequest(sql, params, connection, (resultSet) -> {
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                T row = mapper.mapRow(resultSet);
                result.add(row);
            }
            return result;
        });
    }

    @FunctionalInterface
    private interface SqlFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    @FunctionalInterface
    private interface ResultSetProcessor<T extends ResultSet, R> extends SqlFunction<T, R> {
    }

    private <R> R processRequest(String sql, Object[] params, Connection connection, ResultSetProcessor<ResultSet, R> function) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            ResultSet resultSet = ps.executeQuery();
            return function.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Execute insert query.
     *
     * @param sql    - sql to be executed
     * @param params - sql parameters
     * @return generated primary key
     */
    public Long executeInsertQuery(String sql, Object[] params, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(sql, RETURN_GENERATED_KEYS)) {
            setParameters(ps, params);
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            Long id = null;
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes update/delete queries.
     *
     * @param sql    - sql to be executed
     * @param params - sql parameters
     */
    public void executeUpdateOrDeleteQuery(String sql, Object[] params, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    private enum JdbcTemplateSingleton {
        INSTANCE;

        @SuppressWarnings("NonSerializableFieldInSerializableClass")
        private final JdbcTemplate value = new JdbcTemplate();
    }

    public static JdbcTemplate getInstance() {
        return JdbcTemplate.JdbcTemplateSingleton.INSTANCE.value;
    }
}
