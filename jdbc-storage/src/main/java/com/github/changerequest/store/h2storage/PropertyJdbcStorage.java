package com.github.changerequest.store.h2storage;

import com.github.changerequest.store.h2storage.mapper.RowMapper;
import com.github.changerequest.store.model.Property;

import javax.sql.DataSource;

public class PropertyJdbcStorage extends AbstractJdbcStorage<Property> {
    private static final String SELECT_PROPERTY_BY_ID = "SELECT * FROM property WHERE id=?";
    private static final String DELETE_PROPERTY_BY_ID = "DELETE FROM property WHERE id=?";
    private static final String SELECT_PROPERTIES = "SELECT * FROM property";
    private static final String INSERT_PROPERTY = "INSERT INTO property (name,value) VALUES(?,?)";
    private static final String UPDATE_PROPERTY_BY_ID = "UPDATE property SET key=?,value=? WHERE id=?";


    public PropertyJdbcStorage(DataSource dataSource, JdbcTemplate jdbcTemplate, RowMapper<Property> rowMapper) {
        super(dataSource, jdbcTemplate, rowMapper);
    }

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_PROPERTY_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_PROPERTY_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_PROPERTIES;
    }

    @Override
    public Property save(Property entity) {
        return runInTransaction((connection) -> {
            Long id = jdbcTemplate.executeInsertQuery(INSERT_PROPERTY, toSqlParams(entity.getKey(), entity.getValue()), connection);
            entity.setId(id);
            return entity;
        });
    }

    @Override
    public Property update(Property entity) {
        return runInTransaction((connection) -> {
            jdbcTemplate.executeUpdateOrDeleteQuery(UPDATE_PROPERTY_BY_ID,
                    toSqlParams(entity.getKey(), entity.getValue(), entity.getId()), connection);
            return entity;
        });
    }
}
