package ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned;

import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.error.NotOwnedException;
import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.collection.CollectionInfo;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;
import ru.bardinpetr.itmo.lab5.models.data.collection.IOwnedEntity;

import java.util.List;
import java.util.stream.Stream;

/**
 * DAO decorator for owned objects
 *
 * @param <K> Type of primary key
 * @param <V> Entity type
 */
public class OwnedDAO<K, V extends IKeyedEntity<K> & IOwnedEntity> implements ICollectionDAO<K, V>, IOwnedCollectionDAO<K, V> {
    private final ICollectionDAO<K, V> decoratee;

    public OwnedDAO(ICollectionDAO<K, V> decoratee) {
        this.decoratee = decoratee;
    }

    private boolean checkNotOwned(Integer user, K id) {
        var obj = decoratee.read(id);
        if (obj == null || user == null)
            return true;
        return !obj.getOwner().equals(user);
    }

    /**
     * Inserts object and sets its owner
     *
     * @return primary key assigned
     */
    public K add(Integer user, V obj) {
        obj.setOwner(user);
        return decoratee.add(obj);
    }

    /**
     * Updates object only if it was created by the same user
     *
     * @throws NotOwnedException if user doesn't own object
     */
    public void update(Integer user, K id, V obj) {
        if (checkNotOwned(user, id))
            throw new NotOwnedException();

        obj.setOwner(user);
        decoratee.update(id, obj);
    }

    /**
     * Removes object only if it was created by the same user
     *
     * @throws NotOwnedException if user doesn't own object
     */
    public boolean remove(Integer user, K id) {
        if (checkNotOwned(user, id))
            throw new NotOwnedException();

        return decoratee.remove(id);
    }

    /**
     * Removes all objects owned by a user
     *
     * @throws NotOwnedException if user doesn't own object
     */
    public boolean clear(Integer user) {
        decoratee
                .asStream()
                .filter(i -> i.getOwner().equals(user))
                .map(IKeyedEntity::getPrimaryKey)
                .toList()
                .forEach(decoratee::remove);
        return true;
    }

    /**
     * Clear collection
     */
    @Override
    public void clear() {
        throw new NotOwnedException("unauthorized");
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
        throw new NotOwnedException("unauthorized");
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
        throw new NotOwnedException("unauthorized");
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
        throw new NotOwnedException("unauthorized");
    }


    /**
     * @return collection stats and basic information
     */
    @Override
    public CollectionInfo getCollectionInfo() {
        return decoratee.getCollectionInfo();
    }

    /**
     * Get entity by id
     *
     * @param id entity or null if invalid id
     * @return entity
     */
    @Override
    public V read(K id) {
        return decoratee.read(id);
    }

    /**
     * Get all entities from collection
     *
     * @return list of all items
     */
    @Override
    public List<V> readAll() {
        return decoratee.readAll();
    }

    /**
     * Get all entities from collection as stream
     *
     * @return list of all items
     */
    @Override
    public Stream<V> asStream() {
        return decoratee.asStream();
    }

    /**
     * Check if collection has an object with this key
     *
     * @param id primary key
     * @return true if exists
     */
    @Override
    public boolean has(K id) {
        return decoratee.has(id);
    }

    /**
     * Get maximum entity in collection
     *
     * @return maximal element
     */
    @Override
    public V getMax() {
        return decoratee.getMax();
    }

    /**
     * Get minimum entity in collection
     *
     * @return minimal element
     */
    @Override
    public V getMin() {
        return decoratee.getMin();
    }

    /**
     * Get next available primary key
     *
     * @return primary key could be assigned to new element
     */
    @Override
    public K nextPrimaryKey() {
        return decoratee.nextPrimaryKey();
    }

    @Override
    public List<Organization> getOrganizations() {
        return decoratee.getOrganizations();
    }

    @Override
    public K addOrg(Organization element) {
        return decoratee.addOrg(element);
    }
}
