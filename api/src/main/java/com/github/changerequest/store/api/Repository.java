package com.github.changerequest.store.api;


import com.github.changerequest.store.persistenceapi.StoredEntity;

import java.util.List;

public interface Repository<ID, T extends StoredEntity<ID>> {
    T saveOrUpdate(T e);

    T find(ID id);

    void remove(ID id);

    List<T> getAll();
}
