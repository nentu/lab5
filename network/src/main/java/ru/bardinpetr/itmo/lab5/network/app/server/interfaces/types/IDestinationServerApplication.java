package ru.bardinpetr.itmo.lab5.network.app.server.interfaces.types;

import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;

/**
 * Interface for transport controller to send AppResponses to channel
 */
public interface IDestinationServerApplication<U> {
    void send(U recipient, APICommandResponse response);
}
