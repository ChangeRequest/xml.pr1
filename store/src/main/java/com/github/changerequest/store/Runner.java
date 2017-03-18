package com.github.changerequest.store;

import com.github.changerequest.store.api.impl.*;
import com.github.changerequest.store.h2storage.*;
import com.github.changerequest.store.h2storage.mapper.*;
import com.github.changerequest.store.model.Catalog;
import com.github.changerequest.store.model.Category;
import com.github.changerequest.store.model.Item;
import com.github.changerequest.store.model.Property;
import com.github.changerequest.store.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Runner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    private static final String SIZE_PROPERTY_KEY = "size";
    private static final String MANUFACTURER_PROPERTY_KEY = "manufacturer";

    public static void main(String[] args) {
        log.info("Started Store App at {}", System.currentTimeMillis());
        StoreApp storeApp = createStoreApp();
        log.debug("Creating Testing Data");
//        createTestData(storeApp);
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

        log.info("Finished Store App at {}", System.currentTimeMillis());
    }

    private static StoreApp createStoreApp() {
        DataSource dataSource = ConnectionManager.getInstance().getDataSource();
        JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
        return new StoreApp(new BasketApiImpl(),
                new CategoryRepositoryImpl(categoryStorage(dataSource, jdbcTemplate)),
                new CatalogRepositoryImpl(catalogStorage(dataSource, jdbcTemplate)),
                new ItemRepositoryImpl(itemStorage(dataSource, jdbcTemplate)),
                new PropertyRepositoryImpl(propertyStorage(dataSource, jdbcTemplate)));
    }

    private static Storage<Long, Catalog> catalogStorage(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        RowMapper<Catalog> rowMapper = new CatalogRowMapper();
        Storage<Long, Catalog> storage = new CatalogJdbcStorage(dataSource, jdbcTemplate, rowMapper);
        return storage;
    }

    private static Storage<Long, Category> categoryStorage(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        RowMapper<Category> rowMapper = new CategoryRowMapper();
        Storage<Long, Category> storage = new CategoryJdbcStorage(dataSource, jdbcTemplate, rowMapper);
        return storage;
    }


    private static Storage<Long, Item> itemStorage(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        RowMapper<Item> rowMapper = new ItemRowMapper();
        Storage<Long, Item> storage = new ItemJdbcStorage(dataSource, jdbcTemplate, rowMapper);
        return storage;
    }

    private static Storage<Long, Property> propertyStorage(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        RowMapper<Property> rowMapper = new PropertyRowMapper();
        Storage<Long, Property> storage = new PropertyJdbcStorage(dataSource, jdbcTemplate, rowMapper);
        return storage;
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
        Property smallToySize = storeApp.createProperty(SIZE_PROPERTY_KEY, "10");
        Property cnManufacturer = storeApp.createProperty(MANUFACTURER_PROPERTY_KEY, "CN");
        Item smallToy = storeApp.createItem("Small butterfly", "Small handmade butterfly", 99, singletonList(toys),
                asList(smallToySize, cnManufacturer));
        Property bigToySize = storeApp.createProperty(SIZE_PROPERTY_KEY, "50");
        Item bigToy = storeApp.createItem("Big butterfly", "Big handmade butterfly", 199, singletonList(toys),
                asList(bigToySize, cnManufacturer));
        Property blackJacketSize = storeApp.createProperty(SIZE_PROPERTY_KEY, "S");
        Property uaeManufacturer = storeApp.createProperty(MANUFACTURER_PROPERTY_KEY, "UAE");
        Item blackJacket = storeApp.createItem("Jacket", "Black children jacket", 200, singletonList(cloth),
                asList(blackJacketSize, uaeManufacturer));
        Property redShoesSize = storeApp.createProperty(SIZE_PROPERTY_KEY, "39");
        Property uaManufacturer = storeApp.createProperty(MANUFACTURER_PROPERTY_KEY, "UA");
        Item redShoes = storeApp.createItem("Shoes", "Red handmade shoes", 350, singletonList(cloth),
                asList(redShoesSize, uaManufacturer));

        Catalog childrenCatalog =
                storeApp.createCatalog("Children Catalog", asList(smallToy, bigToy, blackJacket, redShoes));
    }

}
