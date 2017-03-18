package com.github.changerequest.store.h2storage.mapper;

import com.github.changerequest.store.model.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogRowMapper extends RowMapperWithLongId<Catalog> {
    private static final String NAME_COLUMN = "name";

    @Override
    public Catalog mapRow(ResultSet resultSet) throws SQLException {
        Long id = getId(resultSet);
        String name = resultSet.getString(NAME_COLUMN);
        Catalog catalog = new Catalog();
        catalog.setId(id);
        catalog.setName(name);
        return catalog;
    }
}
