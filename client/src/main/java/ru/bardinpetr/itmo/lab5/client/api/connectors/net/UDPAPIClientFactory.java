package ru.bardinpetr.itmo.lab5.client.api.connectors.net;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.connectors.AbstractAPIClientReceiverFactory;
import ru.bardinpetr.itmo.lab5.models.commands.api.InfoCommand;
import ru.bardinpetr.itmo.lab5.network.app.client.impl.SocketAPIClient;
import ru.bardinpetr.itmo.lab5.network.transport.client.UDPClientTransport;

import java.net.InetSocketAddress;

public class UDPAPIClientFactory extends AbstractAPIClientReceiverFactory {


    private final InetSocketAddress serverAddress;

    public UDPAPIClientFactory(InetSocketAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public APIClientConnector create() {
        var connector = new NetworkServerConnector(
                new SocketAPIClient(new UDPClientTransport(serverAddress))
        );

        try {
            connector.call(new InfoCommand());
            return connector;
        } catch (Throwable ignored) {
            System.err.println("Could not connect to server");
            System.exit(1);
        }

        return null;
    }
}
