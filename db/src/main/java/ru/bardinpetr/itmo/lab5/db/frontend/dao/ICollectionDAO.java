package ru.bardinpetr.itmo.lab5.db.frontend.dao;

import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;

/**
 * DAO for operations with filters.
 *
 * @param <K> Type of primary key
 * @param <V> Entity type
 */
public interface ICollectionDAO<K, V extends IKeyedEntity<K>> extends ICollectionReadDAO<K, V>, ICollectionWriteDAO<K, V> {
}
