package com.github.changerequest.store.persistenceapi;

public abstract class AbstractStoredEntity<ID> implements StoredEntity<ID> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractStoredEntity)) return false;

        AbstractStoredEntity<?> that = (AbstractStoredEntity<?>) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
