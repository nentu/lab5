package ru.bardinpetr.itmo.lab5.network.app.server.special.impl;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.special.AbstractOutputTransportApplication;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;

import java.net.SocketAddress;

@Slf4j
public class UDPOutputTransportApplication
        extends AbstractOutputTransportApplication<SocketAddress, SocketMessage> {

    private final JSONSerDesService<APICommandResponse> serDesService;

    public UDPOutputTransportApplication(IServerTransport<SocketAddress, SocketMessage> transport) {
        super(transport);
        serDesService = new JSONSerDesService<>(APICommandResponse.class);
    }

    @Override
    protected SocketMessage serialize(APICommandResponse request) {
        try {
            byte[] data = serDesService.serialize(request);
            return new SocketMessage(data);
        } catch (SerDesException e) {
            return null;
        }
    }
}
