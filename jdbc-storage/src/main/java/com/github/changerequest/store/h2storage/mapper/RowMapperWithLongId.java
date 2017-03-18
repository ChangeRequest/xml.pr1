package com.github.changerequest.store.h2storage.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class RowMapperWithLongId<T> implements RowMapper<T> {
    private static final String ID_COLUMN = "id";

    protected Long getId(ResultSet resultSet) throws SQLException {
        return resultSet.getLong(ID_COLUMN);
    }
}
