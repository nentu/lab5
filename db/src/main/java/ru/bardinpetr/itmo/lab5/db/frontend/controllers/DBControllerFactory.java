package ru.bardinpetr.itmo.lab5.db.frontend.controllers;

import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;

/**
 * Interface for DB controller factories
 */
public interface DBControllerFactory {
    /**
     * Create DBController
     *
     * @return specific DB Controller object
     * @throws DBCreateException if DB can't be created or initialized
     */
    DBController createController() throws DBCreateException;
}
