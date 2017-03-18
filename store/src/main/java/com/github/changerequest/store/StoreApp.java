package com.github.changerequest.store;

import com.github.changerequest.store.api.*;
import com.github.changerequest.store.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;

public class StoreApp {

    private static final Logger log = LoggerFactory.getLogger(StoreApp.class);
    private final Basket basket;
    private BasketApi basketApi;
    private CategoryRepository categoryRepository;
    private CatalogRepository catalogRepository;
    private ItemRepository itemRepository;
    private PropertyRepository propertyRepository;

    public StoreApp(BasketApi basketApi, CategoryRepository categoryRepository, CatalogRepository catalogRepository,
                    ItemRepository itemRepository, PropertyRepository propertyRepository) {
        this.basketApi = basketApi;
        this.categoryRepository = categoryRepository;
        this.catalogRepository = catalogRepository;
        this.itemRepository = itemRepository;
        this.propertyRepository = propertyRepository;
        basket = new Basket();
    }

    public void checkout() {
        log.debug("Doing checkout");
        Map<Item, Integer> items = basketApi.checkout(basket);
        if (items.isEmpty()) {
            log.warn("There are no items to checkout");
            return;
        }
        log.info("Basket has {} items for checkout", items.size());
        String format = "%3s | %30.30s | %10s | %7s ";
        System.out.println(String.format(format, "#", "Title", "Amount", "Price"));
        int index = 1;
        double totalPrice = 0;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            Integer amount = entry.getValue();
            double price = item.getPrice() * amount;
            System.out.println(String.format(format, index++, item.getTitle(), amount, price));
            totalPrice += price;
        }
        System.out.println(String.format("%50s: %.2f ", "Total", totalPrice));
        log.info("Checked out {} with total price {}", items.size(), totalPrice);
        log.debug("Checkout has been finished");
    }

    public void showLastItemsInTheBasket(int n) {
        log.debug("Printing last {} items in the basket", n);
        String format = "%3s | %30.30s | %60.60s | %7s | %20s | %s ";
        System.out.println(String.format(format, "#", "Title", "Description", "Price", "Categories", "Properties"));
        List<Item> lastItems = basketApi.getLast(basket, n);
        if (lastItems.isEmpty()) {
            log.warn("There are no {} last items in the basket", n);
            return;
        }
        for (Item item : lastItems) {
            log.trace("Printing item {}", item);
            System.out.println(String.format(format, item.getId(), item.getTitle(), item.getDescription(),
                    item.getPrice(), item.getCategories(), item.getProperties()));
        }
        System.out.println();
        log.debug("Last {} items have been printed", n);
    }

    public void showAllCatalogs() {
        log.debug("Printing all catalogs");
        List<Catalog> catalogs = catalogRepository.getAll();
        if (catalogs.isEmpty()) {
            log.warn("There are catalogs in the store");
            return;
        }
        for (Catalog catalog : catalogs) {
            log.trace("Printing catalog {}", catalog);
            System.out.println(String.format("Catalog #%d:", catalog.getId()));
            String format = "%3s | %30.30s | %60.60s | %7s | %20s | %s ";
            System.out.println(String.format(format, "#", "Title", "Description", "Price", "Categories", "Properties"));
            List<Item> items = catalog.getItems();
            if (items.isEmpty()) {
                log.warn("There are no items in catalog #{}", catalog.getId());
                continue;
            }
            for (Item item : items) {
                Item loadedItem = itemRepository.find(item.getId());
                log.trace("Printing item {}", loadedItem);
                System.out.println(String.format(format, loadedItem.getId(), loadedItem.getTitle(), loadedItem.getDescription(),
                        loadedItem.getPrice(), loadedItem.getCategories(), loadedItem.getProperties()));
            }
            System.out.println();
        }
        log.debug("All catalogs have been printed");
    }

    public Catalog createCatalog(String name, List<Item> items) {
        log.debug("Creating new catalog {}", name);
        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog.setItems(items);
        Catalog savedCatalog = catalogRepository.saveOrUpdate(catalog);
        log.debug("Created catalog {} with id #{}", catalog.getName(), catalog.getId());
        return savedCatalog;
    }

    public Category createCategory(String title, String description) {
        log.debug("Creating new category {}", title);
        Category category = new Category();
        category.setTitle(title);
        category.setDescription(description);
        Category savedCategory = categoryRepository.saveOrUpdate(category);
        log.debug("Created category {} with id #{}", category.getTitle(), category.getId());
        return savedCategory;
    }

    public Item createItem(String title, String description, double price, List<Category> categories,
                           List<Property> properties) {
        log.debug("Creating new item {}", title);
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setPrice(price);
        item.setCategories(unmodifiableList(categories));
        item.setProperties(unmodifiableList(properties));
        Item savedItem = itemRepository.saveOrUpdate(item);
        log.debug("Created item {} with id #{}", item.getTitle(), item.getId());
        return savedItem;
    }

    public Property createProperty(String key, String value) {
        log.debug("Creating new property {}", key);
        Property property = new Property(key, value);
        Property savedProperty = propertyRepository.saveOrUpdate(property);
        log.debug("Created property {}", property);
        return savedProperty;
    }

    public List<Item> getAllItems() {
        return itemRepository.getAll();
    }

    public void addItemToBasket(Item item) {
        basketApi.add(basket, item);
        log.debug("Item #{} was added to the basket", item.getId());
    }

    public void removeItemFromBasket(Item item) {
        basketApi.remove(basket, item);
        log.debug("Item #{} was removed from basket", item.getId());
    }
}
