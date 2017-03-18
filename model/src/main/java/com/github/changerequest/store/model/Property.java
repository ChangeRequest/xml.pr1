package com.github.changerequest.store.model;

import com.github.changerequest.store.persistenceapi.AbstractStoredEntity;

public class Property extends AbstractStoredEntity<Long> {

    private Long id;
    private String key;
    private String value;

    public Property(Long id, String key, String value) {
        this(key, value);
        this.id = id;
    }

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }
}
