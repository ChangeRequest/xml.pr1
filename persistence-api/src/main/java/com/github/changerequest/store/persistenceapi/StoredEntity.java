package com.github.changerequest.store.persistenceapi;

public interface StoredEntity<ID> {

    ID getId();

    void setId(ID id);
}
