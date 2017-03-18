package com.github.changerequest.store.api.repositories;

import com.github.changerequest.store.storage.Storage;
import com.github.changerequest.store.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ItemRepository {

    private static final Logger log = LoggerFactory.getLogger(ItemRepository.class);

    private Storage<Item, Long> itemStorage;

    public ItemRepository(Storage<Item, Long> itemStorage) {
        if (itemStorage == null) {
            log.error("Failed to create ItemRepository. Storage wasn't specified.");
            throw new IllegalArgumentException();
        }
        this.itemStorage = itemStorage;
    }

    public Item saveOrUpdate(Item item) {
        if (item == null || item.getTitle() == null) {
            log.warn("Trying to save invalid item");
            return item;
        }
        log.trace("Saving item: {}", item);
        if (item.getId() == null) {
            log.debug("Id wasn't specified, creating new item {}", item.getTitle());
            Item createdItem = itemStorage.save(item);
            log.debug("New item {} with id #{} has been created", item.getTitle(), item.getId());
            log.trace("Saved item: {}", item);
            return createdItem;
        }
        log.debug("Id was specified, updating item #{}", item.getId());
        Item updatedItem = itemStorage.update(item);
        log.debug("Item #{} has been updated");
        log.trace("Updated item: {}", item);
        return updatedItem;
    }

    public Item find(Long id) {
        log.debug("Searching for item #{}", id);
        Item item = itemStorage.findOne(id);
        if (item == null) {
            log.warn("Item #{} was not found", id);
        }
        log.trace("Found item: ", item);
        return item;
    }

    public void remove(Long id) {
        if (id == null) {
            log.warn("Attempt to delete item without id");
            return;
        }
        log.debug("Removing item #{}", id);
        itemStorage.delete(id);
        log.debug("Item #{} has been removed", id);
    }

    public List<Item> getAll() {
        log.debug("Searching for all items");
        List<Item> items = itemStorage.findAll();
        log.debug("Found {} items", items.size());
        log.trace("Found items: ", items);
        return items;
    }
}
