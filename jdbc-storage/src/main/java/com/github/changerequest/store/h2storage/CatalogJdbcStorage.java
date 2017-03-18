package com.github.changerequest.store.h2storage;

import com.github.changerequest.store.h2storage.mapper.ItemRowMapper;
import com.github.changerequest.store.h2storage.mapper.RowMapper;
import com.github.changerequest.store.model.Catalog;
import com.github.changerequest.store.model.Item;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class CatalogJdbcStorage extends AbstractJdbcStorage<Catalog> {
    private static final String INSERT_CATALOG = "INSERT INTO catalog (NAME) VALUES (?)";
    private static final String INSERT_CATALOG_ITEM = "INSERT INTO catalog_items (catalog_id, item_id) VALUES(?,?)";
    private static final String DELETE_CATALOG_BY_ID = "DELETE FROM catalog WHERE ID=?";
    private static final String DELETE_CATALOG_ITEMS_BY_CATALOG_ID = "DELETE FROM catalog_items WHERE CATALOG_ID=?";
    private static final String UPDATE_CATALOG_BY_ID = "UPDATE catalog SET name=? WHERE id=?";
    private static final String SELECT_CATALOGS = "SELECT * FROM catalog";
    private static final String SELECT_CATALOG_BY_ID = "SELECT * FROM catalog WHERE id=?";
    private static final String SELECT_ITEMS_BY_CATALOG_ID = "SELECT * FROM item WHERE id in (SELECT item_id FROM catalog_items WHERE catalog_id=?)";

    public CatalogJdbcStorage(DataSource dataSource, JdbcTemplate jdbcTemplate, RowMapper<Catalog> rowMapper) {
        super(dataSource, jdbcTemplate, rowMapper);
    }

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_CATALOG_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_CATALOG_BY_ID;
    }

    @Override
    protected void deleteRelations(final Long id, final Connection connection) {
        jdbcTemplate.executeUpdateOrDeleteQuery(DELETE_CATALOG_ITEMS_BY_CATALOG_ID, toSqlParams(id), connection);
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_CATALOGS;
    }


    @Override
    public Catalog save(Catalog entity) {
        return runInTransaction((connection) -> {
            Long id = jdbcTemplate.executeInsertQuery(INSERT_CATALOG, toSqlParams(entity.getName()), connection);
            entity.setId(id);
            saveCatalogItemRelations(entity);
            return entity;
        });
    }


    private void saveCatalogItemRelations(Catalog catalog) {
        for (Item item : catalog.getItems()) {
            jdbcTemplate.executeInsertQuery(INSERT_CATALOG_ITEM, toSqlParams(catalog.getId(), item.getId()), getConnection());
        }
    }

    @Override
    public Catalog update(Catalog entity) {
        return runInTransaction((connection) -> {
            jdbcTemplate.executeUpdateOrDeleteQuery(UPDATE_CATALOG_BY_ID, toSqlParams(entity.getName(), entity.getId()), connection);
            jdbcTemplate.executeUpdateOrDeleteQuery(DELETE_CATALOG_ITEMS_BY_CATALOG_ID, toSqlParams(entity.getId()), connection);
            saveCatalogItemRelations(entity);
            return entity;
        });
    }

    @Override
    protected void populateRelations(Catalog result, Connection connection) {
        List<Item> items = jdbcTemplate.queryForList(SELECT_ITEMS_BY_CATALOG_ID, toSqlParams(result.getId()), new ItemRowMapper(), connection);
        result.setItems(items);
    }
}
