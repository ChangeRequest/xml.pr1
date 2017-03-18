package com.github.changerequest.store.api.repositories;

import com.github.changerequest.store.model.Category;
import com.github.changerequest.store.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CategoryRepository {

    private static final Logger log = LoggerFactory.getLogger(CategoryRepository.class);

    private Storage<Category, Long> categoryStorage;

    public CategoryRepository(Storage<Category, Long> categoryStorage) {
        if (categoryStorage == null) {
            log.error("Failed to create CategoryRepository. Storage wasn't specified.");
            throw new IllegalArgumentException();
        }
        this.categoryStorage = categoryStorage;
    }

    public Category saveOrUpdate(Category category) {
        if (category == null || category.getTitle() == null) {
            log.warn("Trying to save invalid category");
            return category;
        }
        log.trace("Saving category: {}", category);
        if (category.getId() == null) {
            log.debug("Id wasn't specified, creating new category {}", category.getTitle());
            Category createdCategory = categoryStorage.save(category);
            log.debug("New category {} with id #{} has been created", category.getTitle(), category.getId());
            log.trace("Saved category: {}", category);
            return createdCategory;
        }
        log.debug("Id was specified, updating category #{}", category.getId());
        Category updatedCategory = categoryStorage.update(category);
        log.debug("Category #{} has been updated");
        log.trace("Updated category: {}", category);
        return updatedCategory;
    }

    public Category find(Long id) {
        log.debug("Searching for category #{}", id);
        Category category = categoryStorage.findOne(id);
        if (category == null) {
            log.warn("Category #{} was not found", id);
        }
        log.trace("Found category: ", category);
        return category;
    }

    public void remove(Long id) {
        if (id == null) {
            log.warn("Attempt to delete category without id");
            return;
        }
        log.debug("Removing category #{}", id);
        categoryStorage.delete(id);
        log.debug("Category #{} has been removed", id);
    }

    public List<Category> getAll() {
        log.debug("Searching for all categories");
        List<Category> categories = categoryStorage.findAll();
        log.debug("Found {} categories", categories.size());
        log.trace("Found categories: ", categories);
        return categories;
    }

}
