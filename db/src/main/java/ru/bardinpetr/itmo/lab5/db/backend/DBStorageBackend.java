package ru.bardinpetr.itmo.lab5.db.backend;

import ru.bardinpetr.itmo.lab5.db.errors.DBBackendIOException;
import ru.bardinpetr.itmo.lab5.models.data.collection.CollectionInfo;

public interface DBStorageBackend<T> {
    default void storeCollection(T data) throws DBBackendIOException {
    }

    T loadCollection() throws DBBackendIOException;

    void clearCollection() throws DBBackendIOException;

    CollectionInfo getInfo();
}
