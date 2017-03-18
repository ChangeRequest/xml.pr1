package com.github.changerequest.store.api.impl;

import com.github.changerequest.store.api.PropertyRepository;
import com.github.changerequest.store.model.Property;
import com.github.changerequest.store.storage.Storage;

public class PropertyRepositoryImpl extends AbstractRepositoryImpl<Long, Property> implements PropertyRepository {
    public PropertyRepositoryImpl(Storage<Long, Property> storage) {
        super(storage);
    }

    @Override
    protected Property updateInternal(Property from, Property to) {
        return new Property(from.getId(), to.getKey(), to.getValue());
    }
}
