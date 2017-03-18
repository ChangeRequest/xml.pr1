package com.github.changerequest.store.api.impl;

import com.github.changerequest.store.api.ItemRepository;
import com.github.changerequest.store.model.Item;
import com.github.changerequest.store.storage.Storage;

public class ItemRepositoryImpl extends AbstractRepositoryImpl<Long, Item> implements ItemRepository {
    public ItemRepositoryImpl(Storage<Long, Item> storage) {
        super(storage);
    }

    @Override
    protected Item updateInternal(Item from, Item to) {
        return new Item(from.getId(), to.getTitle(), to.getDescription(), to.getPrice(), to.getProperties(), to.getCategories());
    }
}
