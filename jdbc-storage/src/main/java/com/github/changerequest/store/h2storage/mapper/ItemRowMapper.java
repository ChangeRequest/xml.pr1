package com.github.changerequest.store.h2storage.mapper;

import com.github.changerequest.store.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ItemRowMapper extends RowMapperWithLongId<Item> {
    private static final String TITLE_COLUMN = "title";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PRICE_COLUMN = "price";

    @Override
    public Item mapRow(ResultSet resultSet) throws SQLException {
        Long id = getId(resultSet);
        String title = resultSet.getString(TITLE_COLUMN);
        String description = resultSet.getString(DESCRIPTION_COLUMN);
        double price = resultSet.getDouble(PRICE_COLUMN);
        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }
}
