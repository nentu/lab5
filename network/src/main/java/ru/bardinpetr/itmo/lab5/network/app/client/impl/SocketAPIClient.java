package ru.bardinpetr.itmo.lab5.network.app.client.impl;

import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.models.commands.IAPIMessage;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.app.client.AbstractAPIClient;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IClientTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;

public class SocketAPIClient extends AbstractAPIClient<SocketMessage> {
    private final JSONSerDesService<IAPIMessage> serDesService;

    public SocketAPIClient(IClientTransport<SocketMessage> transport) {
        super(transport);
        serDesService = new JSONSerDesService<>(IAPIMessage.class);
    }

    @Override
    protected SocketMessage serialize(APICommand request) {
        byte[] payload;
        try {
            payload = serDesService.serialize(request);
        } catch (SerDesException e) {
            return null;
        }
        return new SocketMessage(payload);
    }

    @Override
    protected APICommandResponse deserialize(SocketMessage request) {
        APICommandResponse payload;
        try {
            payload = (APICommandResponse) serDesService.deserialize(request.getPayload());
        } catch (SerDesException | ClassCastException ignored) {
            return null;
        }
        return payload;
    }
}
