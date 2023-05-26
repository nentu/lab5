package ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned;

import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.error.NotOwnedException;
import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;
import ru.bardinpetr.itmo.lab5.models.data.collection.IOwnedEntity;

/**
 * @param <K> Type of primary key
 * @param <V> Entity type
 */
public interface IOwnedCollectionDAO<K, V extends IKeyedEntity<K> & IOwnedEntity> extends ICollectionDAO<K, V> {
    /**
     * Inserts object and sets its owner
     *
     * @return primary key assigned
     */
    K add(Integer user, V obj);

    /**
     * Updates object only if it was created by the same user
     *
     * @throws NotOwnedException if user doesn't own object
     */
    void update(Integer user, K id, V obj);

    /**
     * Removes object only if it was created by the same user
     *
     * @throws NotOwnedException if user doesn't own object
     */
    boolean remove(Integer user, K id);

    /**
     * Removes all objects owned by a user
     */
    boolean clear(Integer user);
}
