package com.github.changerequest.store.api;

import com.github.changerequest.store.model.Basket;
import com.github.changerequest.store.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BasketApi {

    private static final Logger log = LoggerFactory.getLogger(BasketApi.class);

    private Basket basket = new Basket();

    public void add(Item item) {
        log.trace("Adding item {}", item);
        basket.addItem(item);
        log.trace("Added item: {}", item);
        log.debug("Item #{} has been added", item.getId());
    }

    public List<Item> getLast(int n) {
        log.trace("Obtaining {} last items", n);
        List<Item> items = basket.getItems();
        int size = items.size();
        if (n < 1 || n > size) {
            log.error("Wrong number of items has been requested {}. There are {} items in the basket.", n, size);
            throw new IllegalArgumentException();
        }
        List<Item> lastItems = Collections.unmodifiableList(items.subList(size - n, size));
        log.trace("Obtained items: {}", items);
        log.debug("{} items of {} have been obtained", lastItems.size(), items.size());
        return lastItems;
    }

    public void remove(Item item) {
        log.trace("Removing item {}", item);
        basket.removeItem(item);
        log.trace("Removed item: {}", item);
        log.debug("Item #{} has been removed", item.getId());
    }

    public Map<Item, Integer> checkout() {
        List<Item> items = basket.getItems();
        log.debug("Collecting items {} for check out", items.size());
        LinkedHashMap<Item, Integer> collectedItems = new LinkedHashMap<>();
        for (Item item : items) {
            Integer amount = collectedItems.get(item);
            if (amount != null) {
                int newAmount = amount + 1;
                collectedItems.replace(item, newAmount);
                log.trace("Increased item '{}' amount to {}", item, newAmount);
            } else {
                collectedItems.put(item, 1);
                log.trace("Item {} has been added to check out", item);
            }
        }
        log.debug("Collected {} unique items", collectedItems.size());
        return collectedItems;
    }

}
