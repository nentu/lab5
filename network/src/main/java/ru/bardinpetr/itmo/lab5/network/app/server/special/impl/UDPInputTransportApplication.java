package ru.bardinpetr.itmo.lab5.network.app.server.special.impl;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.network.app.server.models.Session;
import ru.bardinpetr.itmo.lab5.network.app.server.special.AbstractInputTransportApplication;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;

import java.net.SocketAddress;

@Slf4j
public class UDPInputTransportApplication
        extends AbstractInputTransportApplication<SocketAddress, SocketMessage> {


    private final JSONSerDesService<APICommand> serDesService;

    public UDPInputTransportApplication(IServerTransport<SocketAddress, SocketMessage> transport) {
        super(transport);
        serDesService = new JSONSerDesService<>(APICommand.class);
    }

    @Override
    protected Session<SocketAddress> supplySession(SocketAddress senderID, SocketMessage incomingMessage) {
        return new Session<>(senderID);
    }

    @Override
    protected APICommand deserialize(SocketMessage request) {
        try {
            return serDesService.deserialize(request.getPayload());
        } catch (SerDesException e) {
            return null;
        }
    }
}
