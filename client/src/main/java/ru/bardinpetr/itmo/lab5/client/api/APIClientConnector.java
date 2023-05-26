package ru.bardinpetr.itmo.lab5.client.api;

import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;


/**
 * Interface to call server via APICommand interface
 */
public interface APIClientConnector {

    /**
     * Send APICommand to server and get result
     *
     * @param cmd command to execute on server
     * @return Response with ICommandResponse from execution
     */
    APICommandResponse call(APICommand cmd) throws APIClientException;
}
