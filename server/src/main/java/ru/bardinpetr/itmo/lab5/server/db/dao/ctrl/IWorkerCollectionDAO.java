package ru.bardinpetr.itmo.lab5.server.db.dao.ctrl;

import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

/**
 * DAO for accessing Workers collection
 */
public interface IWorkerCollectionDAO extends ICollectionDAO<Integer, Worker> {
}
