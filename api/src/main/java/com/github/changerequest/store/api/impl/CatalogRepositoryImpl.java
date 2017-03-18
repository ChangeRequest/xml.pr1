package com.github.changerequest.store.api.impl;


import com.github.changerequest.store.api.CatalogRepository;
import com.github.changerequest.store.model.Catalog;
import com.github.changerequest.store.storage.Storage;

public class CatalogRepositoryImpl extends AbstractRepositoryImpl<Long, Catalog> implements CatalogRepository {

    public CatalogRepositoryImpl(Storage<Long, Catalog> storage) {
        super(storage);
    }

    @Override
    protected Catalog updateInternal(Catalog from, Catalog to) {
        return new Catalog(from.getId(), to.getName(), to.getItems());
    }

}
