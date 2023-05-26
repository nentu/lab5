package ru.bardinpetr.itmo.lab5.db.frontend.dao;


import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;

/**
 * DAO write operations.
 *
 * @param <K> Type of primary key
 * @param <V> Entity type
 */
public interface ICollectionWriteDAO<K, V extends IKeyedEntity<K>> {

    /**
     * Insert element into collection
     *
     * @param worker item to insert
     * @return key assigned to object
     */
    K add(V worker);

    /**
     * Replace entity with given id with new contents
     *
     * @param id           entity id to update
     * @param updateWorker new data
     */
    boolean update(K id, V updateWorker);

    /**
     * Remove entity by id
     *
     * @param id entity id
     * @return true if any element deleted
     */
    boolean remove(K id);

    /**
     * Clear collection
     */
    void clear();

    /**
     * Store collection to file
     */
    default void save() {
    }

    K addOrg(Organization element);
}
