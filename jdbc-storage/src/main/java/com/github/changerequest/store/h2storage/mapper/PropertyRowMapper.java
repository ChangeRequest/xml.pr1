package com.github.changerequest.store.h2storage.mapper;

import com.github.changerequest.store.model.Property;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PropertyRowMapper extends RowMapperWithLongId<Property> {
    private static final String NAME_COLUMN = "name";
    private static final String VALUE_COLUMN = "value";

    @Override
    public Property mapRow(ResultSet resultSet) throws SQLException {
        Long id = getId(resultSet);
        String name = resultSet.getString(NAME_COLUMN);
        String value = resultSet.getString(VALUE_COLUMN);
        Property property = new Property(name, value);
        property.setId(id);
        return property;
    }
}
