package com.github.changerequest.store.h2storage.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface used for mapping rows of a ResultSet on a per-row basis. Implementations of this
 * interface perform the actual work of mapping each row to a result object, but don't need to worry about exception
 * handling. SQLExceptions will be caught and handled by the caller.
 * RowMapper objects should be stateless and thus reusable; they are an ideal choice for implementing row-mapping logic
 * in a single place.
 */
public interface RowMapper<T> {

    /**
     * Implementations must implement this method to map each row of data in the ResultSet. This method should not call
     * next() on the ResultSet; it is only supposed to map values of the current row.
     * @param resultSet - the ResultSet to map (pre-initialized for the current row)
     * @return the result object for the current row
     * @throws SQLException - if a SQLException is encountered getting column values (that is, there's no need to catch
     * SQLException)
     */
    T mapRow(ResultSet resultSet) throws SQLException;

}
