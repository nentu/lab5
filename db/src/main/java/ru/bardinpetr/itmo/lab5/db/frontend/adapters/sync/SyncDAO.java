package ru.bardinpetr.itmo.lab5.db.frontend.adapters.sync;

import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.collection.CollectionInfo;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * DAO decorator for access synchronization
 *
 * @param <K> Type of primary key
 * @param <V> Entity type
 */
public class SyncDAO<K, V extends IKeyedEntity<K>> implements ICollectionDAO<K, V> {
    private final ICollectionDAO<K, V> decoratee;
    private final ReentrantReadWriteLock.WriteLock writeLock;
    private final ReentrantReadWriteLock.ReadLock readLock;

    public SyncDAO(ICollectionDAO<K, V> decoratee) {
        this.decoratee = decoratee;

        var lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    /**
     * Clear collection
     */
    @Override
    public void clear() {
        writeLock.lock();
        decoratee.clear();
        writeLock.unlock();
    }

    /**
     * Insert element into collection.
     * In this implementation always throws NotOwnedException. Use method with user parameter
     *
     * @param worker item to insert
     * @return key assigned to object
     */
    @Override
    public K add(V worker) {
        writeLock.lock();
        var res = decoratee.add(worker);
        writeLock.unlock();
        return res;
    }

    /**
     * Replace entity with given id with new contents
     * In this implementation always throws NotOwnedException. Use method with user parameter
     *
     * @param id           entity id to update
     * @param updateWorker new data
     */
    @Override
    public boolean update(K id, V updateWorker) {
        writeLock.lock();
        var res = decoratee.update(id, updateWorker);
        writeLock.unlock();
        return res;
    }

    /**
     * Remove entity by id
     * In this implementation always throws NotOwnedException. Use method with user parameter
     *
     * @param id entity id
     * @return true if any element deleted
     */
    @Override
    public boolean remove(K id) {
        writeLock.lock();
        var res = decoratee.remove(id);
        writeLock.unlock();
        return res;
    }


    /**
     * @return collection stats and basic information
     */
    @Override
    public CollectionInfo getCollectionInfo() {
        readLock.lock();
        var res = decoratee.getCollectionInfo();
        readLock.unlock();
        return res;
    }

    /**
     * Get entity by id
     *
     * @param id entity or null if invalid id
     * @return entity
     */
    @Override
    public V read(K id) {
        readLock.lock();
        var res = decoratee.read(id);
        readLock.unlock();
        return res;
    }

    /**
     * Get all entities from collection
     *
     * @return list of all items
     */
    @Override
    public List<V> readAll() {
        readLock.lock();
        var res = decoratee.readAll();
        readLock.unlock();
        return res;
    }

    /**
     * Get all entities from collection as stream
     *
     * @return list of all items
     */
    @Override
    public Stream<V> asStream() {
        readLock.lock();
        var res = decoratee.readAll().stream();
        readLock.unlock();
        return res;
    }

    /**
     * Check if collection has an object with this key
     *
     * @param id primary key
     * @return true if exists
     */
    @Override
    public boolean has(K id) {
        readLock.lock();
        var res = decoratee.has(id);
        readLock.unlock();
        return res;
    }

    /**
     * Get maximum entity in collection
     *
     * @return maximal element
     */
    @Override
    public V getMax() {
        readLock.lock();
        var res = decoratee.getMax();
        readLock.unlock();
        return res;
    }

    /**
     * Get minimum entity in collection
     *
     * @return minimal element
     */
    @Override
    public V getMin() {
        readLock.lock();
        var res = decoratee.getMin();
        readLock.unlock();
        return res;
    }

    /**
     * Get next available primary key
     *
     * @return primary key could be assigned to new element
     */
    @Override
    public K nextPrimaryKey() {
        readLock.lock();
        var res = decoratee.nextPrimaryKey();
        readLock.unlock();
        return res;
    }

    @Override
    public List<Organization> getOrganizations() {
        readLock.lock();
        var res = decoratee.getOrganizations();
        readLock.unlock();
        return res;
    }

    @Override
    public K addOrg(Organization element) {
        writeLock.lock();
        var res = decoratee.addOrg(element);
        writeLock.unlock();
        return res;
    }
}
