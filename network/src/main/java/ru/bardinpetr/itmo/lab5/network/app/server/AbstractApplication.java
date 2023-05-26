package ru.bardinpetr.itmo.lab5.network.app.server;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.network.app.server.errors.ApplicationBuildException;
import ru.bardinpetr.itmo.lab5.network.app.server.handlers.IApplicationCommandHandler;
import ru.bardinpetr.itmo.lab5.network.app.server.handlers.RequestHandler;
import ru.bardinpetr.itmo.lab5.network.app.server.interfaces.types.IRequestFilter;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Server and client side API extensible controller.
 * Used to be build a request-reply layer over any transport.
 * It is symmetrical, so handling commands on server ends with sending response and on client - with ACK.
 * Moreover, if supported by underlying channel, could be used to make server able to call client at any time within active session.
 * Session handling is not included by default and should be provided via inheritor of SourcingAPIApplication
 */
@Slf4j
public abstract class AbstractApplication implements IRequestFilter {

    private final Map<Class<? extends APICommand>, RequestHandler> commandHandlers = new HashMap<>();
    private RequestHandler anyCommandHandler;
    private AbstractApplication nextApp;

    public AbstractApplication() {

    }

    /**
     * Processes incoming request firstly using nested applications
     * then may be terminated by any of local on() handlers with precedence of single-command handlers.
     * If nested app terminated request or any local terminating handler exist, no further processing done
     *
     * @param request applications request object
     */
    public final void process(AppRequest request) {
        if (!filter(request)) {
            log.debug("Message {} ignored by {}", request.id(), getClass().getSimpleName());
            forwardToNext(request);
            return;
        }

        log.debug("Processing message at {}: {}", getClass().getSimpleName(), request);

        safeProcessCall(request, this::apply);
        if (request.isTerminated()) return;

        var payload = request.payload();
        if (payload == null)
            throw new RuntimeException("Payload null");

        var handler = commandHandlers.keySet().stream().filter(i -> i.isAssignableFrom(payload.getCmdIdentifier())).findFirst().orElse(null);

        for (var curHandler : new RequestHandler[]{
                handler == null ? null : commandHandlers.get(handler),
                anyCommandHandler
        }) {
            if (curHandler == null) continue;

            safeProcessCall(request, this::beforeTermination);
            if (request.isTerminated()) return;

            safeProcessCall(request, curHandler::handle);
            request.terminate();
            return;
        }

        log.debug("Message {} left {}", request.id(), getClass().getSimpleName());

        forwardToNext(request);
    }

    private void forwardToNext(AppRequest request) {
        if (nextApp == null) return;
        safeProcessCall(request, nextApp::process);
    }

    /**
     * Sets next application for current
     *
     * @param next application to be run after any handlers declared for this app
     * @return next application
     */
    public AbstractApplication chain(AbstractApplication next) {
        nextApp = next;
        return next;
    }

    /**
     * Method called by process() before any delegation
     *
     * @param request request to be processed
     */
    protected void apply(AppRequest request) {
    }

    /**
     * Method called by process() when terminating method found and should be run next
     *
     * @param request request to be processed
     */
    protected void beforeTermination(AppRequest request) {
    }

    /**
     * Call another application and handle exceptions with sending error response
     *
     * @param req request to be processed
     * @param app application's process method
     */
    private void safeProcessCall(AppRequest req, Consumer<AppRequest> app) {
        try {
            app.accept(req);
        } catch (Throwable ex) {
            log.error("Execution error: %s".formatted(ex.getMessage()));
            req
                    .response()
                    .status(APIResponseStatus.SERVER_ERROR)
                    .message(ex.getMessage())
                    .send();
        }
    }

    /**
     * Register command handler
     *
     * @param cmd     command to handle
     * @param handler callable
     */
    public final void on(Class<? extends APICommand> cmd, IApplicationCommandHandler handler) {
        if (commandHandlers.containsKey(cmd))
            throw new ApplicationBuildException("Commands should be handled once only");
        commandHandlers.put(cmd, new RequestHandler(handler));
    }

    /**
     * Register one command handler for multiple commands
     *
     * @param cmds    command list to handle
     * @param handler callable
     */
    public final void on(IApplicationCommandHandler handler, Class<? extends APICommand>... cmds) {
        for (var i : cmds)
            commandHandlers.put(i, new RequestHandler(handler));
    }

    /**
     * Subscribe on any incoming request
     *
     * @param handler callable
     */
    public final void on(IApplicationCommandHandler handler) {
        if (anyCommandHandler != null)
            throw new ApplicationBuildException("Only one global handler should exist");
        anyCommandHandler = new RequestHandler(handler);
    }

    public void start() {
    }
}
