package ru.bardinpetr.itmo.lab5.client.api.connectors;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.common.executor.Executor;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;


/**
 * Allows to connect server executor as backend provider in client without using any communication
 */
public class LocalExecutorAPIConnector implements APIClientConnector {
    private final Executor currentExecutor;

    public LocalExecutorAPIConnector(Executor currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    @Override
    public APICommandResponse call(APICommand cmd) {
        return currentExecutor.execute(cmd);
    }
}
