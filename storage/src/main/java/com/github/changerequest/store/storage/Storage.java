package com.github.changerequest.store.storage;

import com.github.changerequest.store.persistenceapi.StoredEntity;

import java.sql.SQLException;
import java.util.List;

public interface Storage<T extends StoredEntity<ID>, ID> {

    T save(T entity) throws SQLException;

    T update(T entity);

    T findOne(ID id);

    void delete(ID id);

    List<T> findAll();

}
