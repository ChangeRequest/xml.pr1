package com.github.changerequest.store.model;

import com.github.changerequest.store.persistenceapi.AbstractStoredEntity;

public class Category extends AbstractStoredEntity<Long> {

    private Long id;
    private String title;
    private String description;

    public Category() {

    }

    public Category(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category#" + id + ": " + title;
    }

}
