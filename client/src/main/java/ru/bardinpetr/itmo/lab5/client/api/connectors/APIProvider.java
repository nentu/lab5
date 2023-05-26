package ru.bardinpetr.itmo.lab5.client.api.connectors;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;

public class APIProvider {
    private static APIClientConnector connector;

    public static APIClientConnector getConnector() {
        return connector;
    }

    public static void setConnector(APIClientConnector connector) {
        APIProvider.connector = connector;
    }
}
