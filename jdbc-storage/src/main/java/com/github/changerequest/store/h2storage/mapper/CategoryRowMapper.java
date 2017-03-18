package com.github.changerequest.store.h2storage.mapper;

import com.github.changerequest.store.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CategoryRowMapper extends RowMapperWithLongId<Category> {
    private static final String TITLE_COLUMN = "title";
    private static final String DESCRIPTION_COLUMN = "description";

    @Override
    public Category mapRow(ResultSet resultSet) throws SQLException {
        Long id = getId(resultSet);
        String title = resultSet.getString(TITLE_COLUMN);
        String description = resultSet.getString(DESCRIPTION_COLUMN);
        Category category = new Category();
        category.setId(id);
        category.setTitle(title);
        category.setDescription(description);
        return category;
    }
}
