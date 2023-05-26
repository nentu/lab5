package ru.bardinpetr.itmo.lab5.client.api.connectors.net;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.app.client.impl.SocketAPIClient;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportTimeoutException;

public class NetworkServerConnector implements APIClientConnector {

    private final SocketAPIClient apiController;

    public NetworkServerConnector(SocketAPIClient apiController) {
        this.apiController = apiController;
    }

    @Override
    public APICommandResponse call(APICommand cmd) throws APIClientException {
        try {
            return apiController.request(cmd);
        } catch (TransportTimeoutException e) {
            throw new APIClientException(e);
        }
    }
}
