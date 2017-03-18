package com.github.changerequest.store.h2storage;

import com.github.changerequest.store.h2storage.mapper.RowMapper;
import com.github.changerequest.store.model.Category;
import com.github.changerequest.store.model.Item;
import com.github.changerequest.store.model.Property;

import javax.sql.DataSource;
import java.sql.Connection;

public class ItemJdbcStorage extends AbstractJdbcStorage<Item> {
    private static final String SELECT_ITEM_BY_ID = "SELECT * FROM item WHERE id=?";
    private static final String DELETE_ITEM_BY_ID = "DELETE FROM item WHERE id=?";
    private static final String SELECT_CATEGORIES = "SELECT * FROM item";
    private static final String INSERT_ITEM = "INSERT INTO item (title,description,price) VALUES(?,?,?)";
    private static final String UPDATE_ITEM_BY_ID = "UPDATE item SET title=?,description=?,price=? WHERE id=?";
    private static final String DELETE_ITEM_PROPERTIES_BY_ITEM_ID = "DELETE FROM item_properties WHERE item_id=?";
    private static final String DELETE_ITEM_CATEGORIES_BY_ITEM_ID = "DELETE FROM item_categories WHERE item_id=?";
    private static final String INSERT_ITEM_PROPERTY = "INSERT INTO item_properties (item_id, property_id) VALUES(?,?)";
    private static final String INSERT_ITEM_CATEGORY = "INSERT INTO item_categories (item_id, property_id) VALUES(?,?)";
    ;


    public ItemJdbcStorage(DataSource dataSource, JdbcTemplate jdbcTemplate, RowMapper<Item> rowMapper) {
        super(dataSource, jdbcTemplate, rowMapper);
    }

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_ITEM_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_ITEM_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_CATEGORIES;
    }

    @Override
    protected void runBeforeDeleteQuery(final Long id, final Connection connection) {
        deleteRelations(id, connection);
    }

    private void deleteRelations(final Long id, final Connection connection) {
        Object[] sqlParams = toSqlParams(id);
        jdbcTemplate.executeUpdateOrDeleteQuery(DELETE_ITEM_PROPERTIES_BY_ITEM_ID, sqlParams, connection);
        jdbcTemplate.executeUpdateOrDeleteQuery(DELETE_ITEM_CATEGORIES_BY_ITEM_ID, sqlParams, connection);
    }

    @Override
    public Item save(Item entity) {
        return runInTransaction((connection) -> {
            Long id = jdbcTemplate.executeInsertQuery(INSERT_ITEM,
                    toSqlParams(entity.getTitle(), entity.getDescription(), entity.getPrice()), connection);
            entity.setId(id);
            saveRelations(entity, connection);
            return entity;
        });
    }

    private void saveRelations(Item item, Connection connection) {
        for (Property property : item.getProperties()) {
            jdbcTemplate.executeInsertQuery(INSERT_ITEM_PROPERTY, toSqlParams(item.getId(), property.getId()), connection);
        }
        for (Category category : item.getCategories()) {
            jdbcTemplate.executeInsertQuery(INSERT_ITEM_CATEGORY, toSqlParams(item.getId(), category.getId()), connection);
        }
    }

    @Override
    public Item update(Item entity) {
        return runInTransaction((connection) -> {
            jdbcTemplate.executeUpdateOrDeleteQuery(UPDATE_ITEM_BY_ID,
                    toSqlParams(entity.getTitle(), entity.getDescription(), entity.getPrice(), entity.getId()), connection);
            deleteRelations(entity.getId(), connection);
            saveRelations(entity, connection);
            return entity;
        });
    }
}
