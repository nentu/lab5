package ru.bardinpetr.itmo.lab5.network.app.client;

import lombok.Setter;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportTimeoutException;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IClientTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.interfaces.IIdentifiableMessage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeoutException;

/**
 * Base class for implementing api clients
 *
 * @param <T> Low level message type
 */
public abstract class AbstractAPIClient<T extends IIdentifiableMessage> {
    private final IClientTransport<T> transport;
    private Long currentMessageId = 0L;

    @Setter
    private Duration timeout = Duration.of(10, ChronoUnit.MINUTES);

    public AbstractAPIClient(IClientTransport<T> transport) {
        this.transport = transport;
    }

    /**
     * Send APICommand request and synchronously wait for response from server
     *
     * @param request request to be sent
     * @return APICommandResponse response on command
     * @throws TimeoutException   if response not arrived in timeout
     * @throws APIClientException if any error raised in process
     */
    public APICommandResponse request(APICommand request) throws TransportTimeoutException, APIClientException {
        var message = serialize(request);
        if (message == null)
            throw new APIClientException("Failed to serialize");

        message.setId(currentMessageId++);

        try {
            transport.send(message);
        } catch (TransportException e) {
            throw new APIClientException(e);
        }

        T reply;
        try {
            reply = transport.receive(timeout);
        } catch (TransportException e) {
            throw new APIClientException(e);
        }

        if (!validateReply(reply)) {
            throw new APIClientException("Not valid id");
        }

        var deserialized = deserialize(reply);
        if (deserialized == null)
            throw new APIClientException("Failed to deserialize");

        return deserialized;
    }

    /**
     * Validate if incoming message is formally correct as response
     *
     * @param reply incoming low-level message
     * @return true if valid
     */
    private boolean validateReply(T reply) {
        return true;
    }

    /**
     * Serialize APICommand into low level T type message object
     *
     * @param request application level request
     * @return serialized transport level message or null if invalid input
     */
    abstract protected T serialize(APICommand request);

    /**
     * Deserialize response on APICommand (APICommandResponse) from low level T type message object
     *
     * @param request low-level request
     * @return deserialized app-level response or null if invalid input
     */
    abstract protected APICommandResponse deserialize(T request);
}
