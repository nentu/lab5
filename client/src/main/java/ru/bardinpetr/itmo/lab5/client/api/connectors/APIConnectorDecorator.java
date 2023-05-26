package ru.bardinpetr.itmo.lab5.client.api.connectors;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;

public class APIConnectorDecorator implements APIClientConnector {
    protected final APIClientConnector decoratee;

    public APIConnectorDecorator(APIClientConnector decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public APICommandResponse call(APICommand cmd) throws APIClientException {
        return decoratee.call(cmd);
    }
}
