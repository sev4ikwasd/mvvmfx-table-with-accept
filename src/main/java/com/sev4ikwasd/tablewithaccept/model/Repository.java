package com.sev4ikwasd.tablewithaccept.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * An interface for a typical repository.
 *
 * @param <T> Model class
 */
public interface Repository<T> {
    /**
     * @return all items from repository
     */
    List<T> getAll();

    /**
     * @param id of item
     * @return {@link java.util.Optional} of item with given id
     */
    Optional<T> getById(UUID id);

    /**
     * @param item to be added
     * @return {@code true} if item was successfully added
     */
    boolean add(T item);

    /**
     * @param item to be removed
     * @return {@code true} if item was successfully removed
     */
    boolean remove(T item);

    /**
     * @param item to be updated
     * @return {@code true} if item was successfully updated
     */
    boolean update(T item);
}
