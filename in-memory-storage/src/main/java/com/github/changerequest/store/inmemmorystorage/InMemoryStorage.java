package com.github.changerequest.store.inmemmorystorage;

import com.github.changerequest.store.persistenceapi.IdGenerator;
import com.github.changerequest.store.persistenceapi.StoredEntity;
import com.github.changerequest.store.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStorage<T extends StoredEntity<ID>, ID> implements Storage<T, ID> {

    private static final Logger log = LoggerFactory.getLogger(InMemoryStorage.class);

    private IdGenerator<ID> idGenerator;
    private Map<ID, T> storage;

    public InMemoryStorage(IdGenerator<ID> idGenerator) {
        if (idGenerator == null) {
            log.error("IdGenerator is null");
            throw new IllegalArgumentException();
        }
        this.idGenerator = idGenerator;
        this.storage = new HashMap<>();
    }

    @Override
    public T save(T entity) {
        log.trace("Saving {}", entity);
        if (entity.getId() != null) {
            log.error("Attempt to save entity with id");
            throw new IllegalArgumentException();
        }
        entity.setId(idGenerator.generateId());
        storage.put(entity.getId(), entity);
        log.trace("Saved entity: {}", entity);
        log.debug("Item #{} has been saved", entity.getId());
        return entity;
    }

    @Override
    public T update(T entity) {
        log.trace("Updating {}", entity);
        if (entity.getId() == null || !storage.containsKey(entity.getId())) {
            log.error("Specified id is null or doesn't exist");
            throw new IllegalArgumentException();
        }
        storage.replace(entity.getId(), entity);
        log.trace("Updated entity: {}", entity);
        log.debug("Item #{} has been updated", entity.getId());
        return entity;
    }

    @Override
    public T findOne(ID id) {
        log.trace("Searching for #{}", id);
        if (!storage.containsKey(id)) {
            log.debug("Item {} doesn't exists", id);
        }
        T entity = storage.get(id);
        log.trace("Search result is {}", entity);
        return entity;
    }

    @Override
    public void delete(ID id) {
        log.trace("Deleting {}", id);
        T entity = storage.remove(id);
        log.trace("Deleted entity: {}", entity);
        log.debug("Item #{} has been deleted", id);
    }

    @Override
    public List<T> findAll() {
        log.trace("Searching for all entities");
        List<T> entities = Collections.unmodifiableList(new ArrayList<T>(storage.values()));
        log.trace("Found entities: {}", entities);
        log.debug("Found {} entities", entities.size());
        return entities;
    }
}
