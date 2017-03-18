package com.github.changerequest.store.api.impl;

import com.github.changerequest.store.api.Repository;
import com.github.changerequest.store.persistenceapi.StoredEntity;
import com.github.changerequest.store.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public abstract class AbstractRepositoryImpl<ID, T extends StoredEntity<ID>> implements Repository<ID, T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    private final Storage<ID, T> storage;

    public AbstractRepositoryImpl(Storage<ID, T> storage) {
        this.storage = storage;
    }

    @Override
    public T saveOrUpdate(final T e) {
        final T entity = Objects.requireNonNull(e, "Entity could not be null");
        log.trace("Received entity {}", entity);
        if (entity.getId() == null) {
            log.debug("Id wasn't specified, creating new entity {}", entity);
            return storage.save(entity);
        }
        T fromStorage = storage.findOne(entity.getId());
        if (fromStorage == null) {
            log.debug("Entity with given id #{} wasn't found in storage - new entity will be saved.", entity.getId());
            return storage.save(entity);
        }
        log.debug("Id was specified, updating entity #{}", entity.getId());
        return updateInternal(fromStorage, entity);
    }

    protected abstract T updateInternal(final T from, final T to);

    protected Storage<ID, T> getStorage() {
        return storage;
    }

    @Override
    public T find(ID id) {
        log.debug("Searching for entity with id={}", id);
        T entity = storage.findOne(id);
        if (entity == null) {
            log.warn("No entity with id={} was found", id);
        }
        log.trace("Found {} entity", entity);
        return entity;
    }

    @Override
    public void remove(ID id) {
        log.debug("Removing entity with id={}", id);
        storage.delete(id);
        log.debug("Entity with id={} was removed", id);
    }

    @Override
    public List<T> getAll() {
        log.debug("Searching for all entities");
        List<T> result = storage.findAll();
        log.debug("Found {} entities", result.size());
        log.trace("Found entities:\n{}", result);
        return result;
    }
}
