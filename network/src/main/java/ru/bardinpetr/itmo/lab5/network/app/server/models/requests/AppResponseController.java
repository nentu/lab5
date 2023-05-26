package ru.bardinpetr.itmo.lab5.network.app.server.models.requests;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.network.app.server.interfaces.types.IDestinationServerApplication;

/**
 * Object for handling the process of response building and sending it.
 * Message is marked as terminated when it is no need in continuation of its further processing
 *
 * @param <U> user identifier
 */
@Slf4j
public class AppResponseController<U> {

    private final IDestinationServerApplication<U> destination;
    private final Long id;
    private final U recipient;
    private final AppRequest parentRequest;
    private APICommandResponse response;

    public AppResponseController(AppRequest request, IDestinationServerApplication<U> dst) {
        this.parentRequest = request;
        this.recipient = (U) request.session().getAddress();
        this.response = new APICommandResponse();
        this.destination = dst;
        this.id = request.id();
    }

    /**
     * Set response status
     */
    public AppResponseController<U> status(APIResponseStatus status) {
        response.setStatus(status);
        log.debug("Response {} is marked as {}", id, status);
        return this;
    }

    /**
     * @return status of response
     */
    public APIResponseStatus getStatus() {
        return response.getStatus();
    }

    /**
     * Set response textual message
     */
    public AppResponseController<U> message(String text) {
        response.setTextualResponse(text);
        return this;
    }

    /**
     * Call IDestinationServerApplication to send prepared message.
     * Automatically terminates message
     */
    public void send() {
        if (parentRequest.isTerminated()) return;
        parentRequest.terminate();

        if (response.getStatus() == APIResponseStatus.UNPROCESSED)
            response.setStatus(APIResponseStatus.OK);

        destination.send(recipient, response);
        log.debug("Response {} is sent", id);
    }

    /**
     * Set current APICommandResponse from external object
     *
     * @param resp prepared response object
     */
    public AppResponseController<U> from(APICommandResponse resp) {
        response = resp;
        return this;
    }

    /**
     * @return response id
     */
    public Long getId() {
        return id;
    }

    public void sendOk() {
        status(APIResponseStatus.OK).message("OK").send();
    }

    public void sendErr(String err) {
        status(APIResponseStatus.CLIENT_ERROR).message(err).send();
    }
}
