package com.github.changerequest.store.storage;

import com.github.changerequest.store.persistenceapi.StoredEntity;

import java.util.List;

public interface Storage<ID, T extends StoredEntity<ID>> {
    /**
     * Save new instance of {@code T} in storage.
     *
     * @param entity instance to be saved in storage
     * @return saved instance
     * @throws NullPointerException     if {@code e} is null
     * @throws IllegalArgumentException if instance with such {@code ID} is already presented in storage
     */
    T save(T entity);
    /**
     * Update instance of {@code T} in storage
     *
     * @param entity new value of {@code T}
     * @return updated instance
     * @throws IllegalArgumentException if instance with {@code ID} of {@code e} is not presented in storage
     */
    T update(T entity);
    /**
     * Read {@code T} from storage by given {@code ID}.
     *
     * @param id id of object to be read from storage
     * @return object from storage or null if no object with given ID was found
     */
    T findOne(ID id);
    /**
     * Delete instance of {@code T} from storage
     *
     * @param id @{code ID} of instance to be deleted
     */
    void delete(ID id);
    /**
     * Return all instances of {@code T} from storage
     *
     * @return list of all {@code T} instances of empty list if storage is empty
     */
    List<T> findAll();

}
