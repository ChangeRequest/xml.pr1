package com.github.changerequest.store.h2storage;

import com.github.changerequest.store.h2storage.mapper.RowMapper;
import com.github.changerequest.store.model.Category;

import javax.sql.DataSource;

public class CategoryJdbcStorage extends AbstractJdbcStorage<Category> {
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id=?";
    private static final String DELETE_CATEGORY_BY_ID = "DELETE FROM category WHERE id=?";
    private static final String SELECT_CATEGORIES = "SELECT * FROM category";
    private static final String INSERT_CATEGORY = "INSERT INTO category (title,description) VALUES(?,?)";
    private static final String UPDATE_CATEGORY_BY_ID = "UPDATE category SET title=?,description=? WHERE id=?";


    public CategoryJdbcStorage(DataSource dataSource, JdbcTemplate jdbcTemplate, RowMapper<Category> rowMapper) {
        super(dataSource, jdbcTemplate, rowMapper);
    }

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_CATEGORY_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_CATEGORY_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_CATEGORIES;
    }

    @Override
    public Category save(Category entity) {
        return runInTransaction((connection) -> {
            Long id = jdbcTemplate.executeInsertQuery(INSERT_CATEGORY, toSqlParams(entity.getTitle(), entity.getDescription()), connection);
            entity.setId(id);
            return entity;
        });
    }

    @Override
    public Category update(Category entity) {
        return runInTransaction((connection) -> {
            jdbcTemplate.executeUpdateOrDeleteQuery(UPDATE_CATEGORY_BY_ID,
                    toSqlParams(entity.getTitle(), entity.getDescription(), entity.getId()), connection);
            return entity;
        });
    }
}
