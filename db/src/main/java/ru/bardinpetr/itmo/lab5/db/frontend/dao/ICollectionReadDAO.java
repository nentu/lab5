package ru.bardinpetr.itmo.lab5.db.frontend.dao;

import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.collection.CollectionInfo;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * DAO read operations.
 *
 * @param <K> Type of primary key
 * @param <V> Entity type
 */
public interface ICollectionReadDAO<K, V extends IKeyedEntity<K>> {
    /**
     * @return collection stats and basic information
     */
    CollectionInfo getCollectionInfo();

    /**
     * Get entity by id
     *
     * @param id entity or null if invalid id
     * @return entity
     */
    V read(K id);

    /**
     * Get all entities from collection
     *
     * @return list of all items
     */
    List<V> readAll();

    /**
     * Get all entities from collection as stream
     *
     * @return list of all items
     */
    Stream<V> asStream();

    /**
     * Get all entities from collection in order using comparator
     *
     * @param order comparator to apply
     * @return sorted list of all entities
     */
    default List<V> readAll(Comparator<V> order) {
        return readAll().stream().sorted(order).toList();
    }

    /**
     * Check if collection has an object with this key
     *
     * @param id primary key
     * @return true if exists
     */
    boolean has(K id);

    /**
     * Return all items from collection matching predicate
     *
     * @param condition if predicate is true item is included
     * @return list of filtered items
     */
    default List<V> filter(Predicate<V> condition) {
        return readAll().stream().filter(condition).toList();
    }

    /**
     * Returns all elements with function applied to each
     *
     * @param extractor function to map from collection item to output object
     * @return list of extracted fields for all items
     */
    default <T> List<T> getAllMapped(Function<V, T> extractor) {
        return readAll().stream().map(extractor).toList();
    }

    /**
     * Get maximum entity in collection
     *
     * @return maximal element
     */
    V getMax();


    /**
     * Get minimum entity in collection
     *
     * @return minimal element
     */
    V getMin();

    /**
     * Get next available primary key
     *
     * @return primary key could be assigned to new element
     */
    K nextPrimaryKey();

    List<Organization> getOrganizations();
}
