package com.github.changerequest.store.api.repositories;

import com.github.changerequest.store.model.Catalog;
import com.github.changerequest.store.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CatalogRepository {

    private static final Logger log = LoggerFactory.getLogger(CatalogRepository.class);

    private Storage<Catalog, Long> catalogStorage;

    public CatalogRepository(Storage<Catalog, Long> catalogStorage) {
        if (catalogStorage == null) {
            log.error("Failed to create CatalogRepository. Storage wasn't specified.");
            throw new IllegalArgumentException();
        }
        this.catalogStorage = catalogStorage;
    }

    public Catalog saveOrUpdate(Catalog catalog) {
        if (catalog == null || catalog.getName() == null) {
            log.warn("Trying to save invalid catalog");
            return catalog;
        }
        log.trace("Saving catalog: {}", catalog);
        if (catalog.getId() == null) {
            log.debug("Id wasn't specified, creating new catalog {}", catalog.getName());
            Catalog createdCatalog = catalogStorage.save(catalog);
            log.debug("New catalog {} with id #{} has been created", catalog.getName(), catalog.getId());
            log.trace("Saved catalog: {}", catalog);
            return createdCatalog;
        }
        log.debug("Id was specified, updating catalog #{}", catalog.getId());
        Catalog updatedCatalog = catalogStorage.update(catalog);
        log.debug("Catalog #{} has been updated");
        log.trace("Updated catalog: {}", catalog);
        return updatedCatalog;
    }

    public Catalog find(Long id) {
        log.debug("Searching for catalog #{}", id);
        Catalog catalog = catalogStorage.findOne(id);
        if (catalog == null) {
            log.warn("Catalog #{} was not found", id);
        }
        log.trace("Found catalog: ", catalog);
        return catalog;
    }

    public void remove(Long id) {
        if (id == null) {
            log.warn("Attempt to delete category without id");
            return;
        }
        log.debug("Removing catalog #{}", id);
        catalogStorage.delete(id);
        log.debug("Catalog #{} has been removed", id);
    }

    public List<Catalog> getAll() {
        log.debug("Searching for all catalogs");
        List<Catalog> catalogs = catalogStorage.findAll();
        log.debug("Found {} catalogs", catalogs.size());
        log.trace("Found catalogs: ", catalogs);
        return catalogs;
    }

}
