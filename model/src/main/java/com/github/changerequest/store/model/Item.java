package com.github.changerequest.store.model;

import com.github.changerequest.store.persistenceapi.AbstractStoredEntity;

import java.util.List;

public class Item extends AbstractStoredEntity<Long> {

    private Long id;
    private String title;
    private String description;
    private double price;
    private List<Property> properties;
    private List<Category> categories;

    public Item() {

    }

    public Item(Long id, String title, String description, double price, List<Property> properties, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.properties = properties;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Item#" + id + ": " + title;
    }

}
