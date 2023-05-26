package ru.bardinpetr.itmo.lab5.network.app.server.special;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.models.Session;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppResponseController;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;

/**
 * Application that should supply AppMessages with reply controllers
 * and handle how to send responses after processing AppRequest back to transport.
 * Should be one of first in chain as in other way no app will be able to send response
 *
 * @param <U> user identifier type
 * @param <L> low-level message object to be used as source for AppRequest
 */
@Slf4j
public abstract class AbstractOutputTransportApplication<U, L> extends AbstractApplication {

    private final IServerTransport<U, L> transport;

    public AbstractOutputTransportApplication(IServerTransport<U, L> transport) {
        this.transport = transport;
    }

    @Override
    public void apply(AppRequest request) {
        var resp = supplyResponse(request);
        request.setResponse(resp);
        request.session().setState(Session.State.OPERATING);
        request.setStatus(AppRequest.ReqStatus.NORMAL);

        log.info("Message from {} initialized as {}", request.session().getAddress(), request.status());
    }

    /**
     * Send prepared AppResponse to transport layer
     *
     * @param response response with destination and payload set
     */
    protected void send(U recipient, APICommandResponse response) {
        var serialized = serialize(response);
        if (serialized == null) {
            log.warn("Failed to serialize message for {}", recipient);
            return;
        }

        log.info("Sending {} to {}", response, recipient);
        transport.send(recipient, serialized);
    }

    /**
     * Create response controller for specified session
     * Called by handle() on new message arrived
     *
     * @param request incoming message to create response for
     */
    protected <T extends APICommand> AppResponseController<U> supplyResponse(AppRequest request) {
        return new AppResponseController<>(request, this::send);
    }

    /**
     * Serialize message reply (A) into low level L type message object.
     * Used to send replies on executed APICommand
     *
     * @param request application level request
     * @return serialized transport level message or null if invalid input
     */
    abstract protected L serialize(APICommandResponse request);
}
