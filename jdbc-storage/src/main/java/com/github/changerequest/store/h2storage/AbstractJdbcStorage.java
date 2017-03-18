package com.github.changerequest.store.h2storage;

import com.github.changerequest.store.h2storage.mapper.RowMapper;
import com.github.changerequest.store.persistenceapi.StoredEntity;
import com.github.changerequest.store.storage.Storage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractJdbcStorage<T extends StoredEntity<Long>> implements Storage<Long, T> {
    private Connection connection;
    protected final DataSource dataSource;
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

    protected AbstractJdbcStorage(DataSource dataSource, JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public T findOne(final Long id) {
        return runInTransaction((connection) -> {
            final T result = jdbcTemplate.queryForObject(getSelectByIdQuery(), toSqlParams(id), rowMapper, connection);
            populateRelations(result, connection);
            return result;
        });
    }

    protected void populateRelations(T result, Connection connection) {

    }

    protected abstract String getSelectByIdQuery();

    @Override
    public void delete(final Long id) {
        runInTransaction((connection) -> {
            deleteRelations(id, connection);
            jdbcTemplate.executeUpdateOrDeleteQuery(getDeleteByIdQuery(), toSqlParams(id), connection);
            return null;
        });
    }

    protected void deleteRelations(final Long id, final Connection connection) {

    }

    protected abstract String getDeleteByIdQuery();

    @Override
    public List<T> findAll() {
        return runInTransaction((connection) -> {
            final List<T> result = jdbcTemplate.queryForList(getSelectAllQuery(), new Object[]{}, rowMapper, connection);
            for (T entity : result) {
                populateRelations(entity, connection);
            }
            return result;
        });
    }

    protected abstract String getSelectAllQuery();

    protected Object[] toSqlParams(Object... params) {
        return params;
    }

    protected void startTransaction() {
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void endTransaction() {
        try {
            connection.commit();
            connection = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() {
        if (connection == null) {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    protected void rollback() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    protected <R> R runInTransaction(Function<Connection, R> supplier) {
        try {
            startTransaction();
            R result = supplier.apply(getConnection());
            endTransaction();
            return result;
        } catch (Throwable e) {
            rollback();
            throw e;
        }
    }
}
