package com.github.changerequest.store.api.impl;

import com.github.changerequest.store.api.CategoryRepository;
import com.github.changerequest.store.model.Category;
import com.github.changerequest.store.storage.Storage;

public class CategoryRepositoryImpl extends AbstractRepositoryImpl<Long, Category> implements CategoryRepository {
    public CategoryRepositoryImpl(Storage<Long, Category> storage) {
        super(storage);
    }

    @Override
    protected Category updateInternal(Category from, Category to) {
        return new Category(from.getId(), to.getTitle(), to.getDescription());
    }
}
