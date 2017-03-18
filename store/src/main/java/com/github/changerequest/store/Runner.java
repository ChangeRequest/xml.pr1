package com.github.changerequest.store;

import com.github.changerequest.store.api.BasketApi;
import com.github.changerequest.store.api.repositories.CatalogRepository;
import com.github.changerequest.store.api.repositories.CategoryRepository;
import com.github.changerequest.store.api.repositories.ItemRepository;
import com.github.changerequest.store.inmemmorystorage.InMemoryStorage;
import com.github.changerequest.store.inmemmorystorage.LongIdGenerator;
import com.github.changerequest.store.model.Catalog;
import com.github.changerequest.store.model.Category;
import com.github.changerequest.store.model.Item;
import com.github.changerequest.store.model.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Runner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        log.info("Started Store App at {}", System.currentTimeMillis());
        StoreApp storeApp = createStoreApp();
        log.debug("Creating Testing Data");
        createTestData(storeApp);
        log.debug("Printing Catalogs");
        storeApp.showAllCatalogs();
        log.debug("Adding items to basket");
        List<Item> itemsInTheBasket = putItemsIntoBasket(storeApp, 20);
        log.info("Removing items to basket");
        List<Item> remainItemsInTheBasket = removeItemsFromBasket(storeApp, new ArrayList<>(itemsInTheBasket));
        log.debug("Printing last items in the basket");
        storeApp.showLastItemsInTheBasket(countUniqueItems(remainItemsInTheBasket) / 2);
        log.debug("Doing checkout");
        storeApp.checkout();

        log.info("Testing warn+ logging for storage module");
        testLoggingForStorage(20);
        log.info("Testing warn logging for api module");
        testLoggingForApi(20);
        log.info("Finished Store App at {}", System.currentTimeMillis());
    }

    private static StoreApp createStoreApp() {
        return new StoreApp(new BasketApi(),
                new CategoryRepository(new InMemoryStorage<>(new LongIdGenerator())),
                new CatalogRepository(new InMemoryStorage<>(new LongIdGenerator())),
                new ItemRepository(new InMemoryStorage<>(new LongIdGenerator())));
    }

    private static void testLoggingForApi(int count) {
        ItemRepository itemRepository = new ItemRepository(new InMemoryStorage<>(new LongIdGenerator()));
        for (int i = 0; i < count; ++i) {
            itemRepository.find(null);
            itemRepository.saveOrUpdate(null);
            itemRepository.remove(null);
        }
    }

    private static void testLoggingForStorage(int count) {
        InMemoryStorage<Item, Long> inMemoryStorage = new InMemoryStorage<>(new LongIdGenerator());
        for (int i = 0; i < count; ++i) {
            try {
                Item item = new Item();
                inMemoryStorage.update(item);
            } catch (IllegalArgumentException ex) {
                log.debug("Exception update for item that doesn't exist was caught as expected");
            }
        }
    }

    private static List<Item> removeItemsFromBasket(StoreApp storeApp, List<Item> itemsInTheBasket) {
        if (itemsInTheBasket.isEmpty()) {
            log.warn("Nothing to remove from basket");
            return emptyList();
        }
        int toRemoveCount = itemsInTheBasket.size() / 2;
        int removed = 0;
        Iterator<Item> iterator = itemsInTheBasket.iterator();
        while (iterator.hasNext() && removed < toRemoveCount) {
            Item item = iterator.next();
            storeApp.removeItemFromBasket(item);
            iterator.remove();
            ++removed;
        }
        log.info("{} items were removed from basket", removed);
        return itemsInTheBasket;
    }

    private static List<Item> putItemsIntoBasket(StoreApp storeApp, int count) {
        List<Item> items = storeApp.getAllItems();
        if (items.isEmpty()) {
            log.warn("There are no items in the store");
            return emptyList();
        }
        List<Item> addedItems = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; ++i) {
            Item item = items.get(random.nextInt(items.size()));
            addedItems.add(item);
            storeApp.addItemToBasket(item);
        }
        log.info("{} items were added from basket", addedItems.size());
        return addedItems;
    }

    private static int countUniqueItems(List<Item> items) {
        return new HashSet<>(items).size();
    }

    private static void createTestData(StoreApp storeApp) {
        Category toys = storeApp.createCategory("Toys", "Toys for children");
        Category cloth = storeApp.createCategory("Cloth", "Cloth for children");

        Item smallToy = storeApp.createItem("Small butterfly", "Small handmade butterfly", 99, singletonList(toys),
                asList(new Property("size", "10"), new Property("manufacturer", "CN")));
        Item bigToy = storeApp.createItem("Big butterfly", "Big handmade butterfly", 199, singletonList(toys),
                asList(new Property("size", "50"), new Property("manufacturer", "CN")));

        Item blackJacket = storeApp.createItem("Jacket", "Black children jacket", 200, singletonList(cloth),
                asList(new Property("size", "S"), new Property("manufacturer", "UAE")));
        Item redShoes = storeApp.createItem("Shoes", "Red handmade shoes", 350, singletonList(cloth),
                asList(new Property("size", "39"), new Property("manufacturer", "UA")));

        Catalog childrenCatalog =
                storeApp.createCatalog("Children Catalog", asList(smallToy, bigToy, blackJacket, redShoes));
    }

}
