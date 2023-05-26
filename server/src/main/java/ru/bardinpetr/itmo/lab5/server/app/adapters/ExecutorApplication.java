package ru.bardinpetr.itmo.lab5.server.app.adapters;

import ru.bardinpetr.itmo.lab5.common.executor.Executor;
import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;

public class ExecutorApplication extends AbstractApplication {

    public ExecutorApplication() {
    }

    /**
     * Add executor as subsequent application
     *
     * @param executor executor to be wrapped with ExecutorAdapterApplication
     */
    public AbstractApplication chain(Executor executor) {
        var adapter = new ExecutorAdapterApplication(executor);
        chain(adapter);
        return adapter;
    }
}



