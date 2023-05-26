package ru.bardinpetr.itmo.lab5.client.api.connectors;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;

public abstract class AbstractAPIClientReceiverFactory {
    public abstract APIClientConnector create();
}
