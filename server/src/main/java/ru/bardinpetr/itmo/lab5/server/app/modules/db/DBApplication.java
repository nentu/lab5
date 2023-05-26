package ru.bardinpetr.itmo.lab5.server.app.modules.db;

import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.IOwnedCollectionDAO;
import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.OwnedDAO;
import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.APIApplication;
import ru.bardinpetr.itmo.lab5.server.app.adapters.ExecutorAdapterApplication;

public class DBApplication extends APIApplication {
    private final IOwnedCollectionDAO<Integer, Worker> dao;

    public DBApplication(ICollectionDAO<Integer, Worker> originalDAO) {
        this.dao = new OwnedDAO<>(originalDAO);

        this.chain(new ExecutorAdapterApplication(new DBReadExecutor(dao)))
                .chain(new ExecutorAdapterApplication(new DBPagingExecutor(dao)))
                .chain(new DBInsertApplication(dao))
                .chain(new DBUpdateApplication(dao))
                .chain(new DBRemoveApplication(dao))
                .chain(new DBAuthedApplication(dao));

        addExitHook();
    }

    private void addExitHook() {
        Runtime
                .getRuntime()
                .addShutdownHook(new Thread(dao::save));
    }
}
