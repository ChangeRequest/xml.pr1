package com.github.changerequest.store.model;

import com.github.changerequest.store.persistenceapi.StoredEntity;

public class Property implements StoredEntity<Long> {

    private Long id;
    private String key;
    private String value;

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
