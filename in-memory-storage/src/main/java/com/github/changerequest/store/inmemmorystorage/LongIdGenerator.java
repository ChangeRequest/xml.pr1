package com.github.changerequest.store.inmemmorystorage;

import com.github.changerequest.store.persistenceapi.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongIdGenerator implements IdGenerator<Long> {

    private static final Logger log = LoggerFactory.getLogger(LongIdGenerator.class);

    private long nextId = 1;

    @Override
    public Long generateId() {
        long id = nextId++;
        log.debug("Generated new id {}", id);
        return id;
    }
}
