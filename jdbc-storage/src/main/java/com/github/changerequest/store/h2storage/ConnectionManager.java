package com.github.changerequest.store.h2storage;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static final int MAX_CONNECTIONS = 5;
    private final JdbcDataSource dataSource;
    private final JdbcConnectionPool connectionPool;

    private ConnectionManager() {
        dataSource = prepareDataSource();
        connectionPool = prepareConnectionPool(dataSource);
    }

    private JdbcConnectionPool prepareConnectionPool(final ConnectionPoolDataSource connectionPoolDataSource) {
        final JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(connectionPoolDataSource);
        jdbcConnectionPool.setMaxConnections(MAX_CONNECTIONS);
        return jdbcConnectionPool;
    }

    private JdbcDataSource prepareDataSource() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(DbConfigHolder.getUrl());
        dataSource.setUser(DbConfigHolder.getUserName());
        dataSource.setPassword(DbConfigHolder.getPassword());
        return dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void close() {
        connectionPool.dispose();
    }

    private enum ConnectionManagerSingleton {
        INSTANCE;

        @SuppressWarnings("NonSerializableFieldInSerializableClass")
        private final ConnectionManager value = new ConnectionManager();
    }
    public static ConnectionManager getInstance() {
        return ConnectionManagerSingleton.INSTANCE.value;
    }
}
