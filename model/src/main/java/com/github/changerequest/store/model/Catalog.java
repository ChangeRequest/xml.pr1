package com.github.changerequest.store.model;

import com.github.changerequest.store.persistenceapi.StoredEntity;

import java.util.List;

public class Catalog implements StoredEntity<Long> {

    private Long id;
    private String name;
    private List<Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Catalog#"+ id + ": " + name;
    }
}
