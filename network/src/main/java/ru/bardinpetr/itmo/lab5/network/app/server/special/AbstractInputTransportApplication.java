package ru.bardinpetr.itmo.lab5.network.app.server.special;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.models.Session;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.interfaces.IIdentifiableMessage;

/**
 * Represents Application capable of sending app-level messages to app-chain build from transport-level messages.
 * Message source (first application in chain) should implement this class.
 *
 * @param <U> user identifier type
 * @param <L> low-level message object to be used as source for AppRequest
 */
@Slf4j
public abstract class AbstractInputTransportApplication<U, L extends IIdentifiableMessage> extends AbstractApplication {

    private final IServerTransport<U, L> transport;

    public AbstractInputTransportApplication(IServerTransport<U, L> transport) {
        this.transport = transport;
        transport.subscribe(this::handle);
    }

    /**
     * Process in incoming message arrived from underlying protocol
     *
     * @param senderID        low-level user identifier
     * @param incomingMessage low-level message
     */
    private void handle(U senderID, L incomingMessage) {
        var status = AppRequest.ReqStatus.CREATED;

        log.info("New transport message from {}", senderID);

        var msg = deserialize(incomingMessage);
        if (msg == null) {
            log.warn("Failed to deserialize message from {}", senderID);
            status = AppRequest.ReqStatus.INVALID;
        }

        var session = supplySession(senderID, incomingMessage);

        var appRequest = new AppRequest(
                status,
                incomingMessage.getId(),
                session,
                null,
                msg,
                false
        );

        process(appRequest);
    }

    /**
     * Create session object for specific message from transport layer.
     * Called by handle() on new message arrived
     *
     * @param senderID        low-level user identifier
     * @param incomingMessage low-level message
     */
    protected abstract Session<U> supplySession(U senderID, L incomingMessage);

    /**
     * Deserialize incoming request (Q) from low level L type message object
     *
     * @param request low-level request
     * @return deserialized app-level response or null if invalid input
     */
    protected abstract APICommand deserialize(L request);

    @Override
    public boolean filter(AppRequest req) {
        return true;
    }

    @Override
    public void start() {
        transport.run();
    }
}
