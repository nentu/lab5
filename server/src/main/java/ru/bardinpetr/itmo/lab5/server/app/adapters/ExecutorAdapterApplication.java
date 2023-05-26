package ru.bardinpetr.itmo.lab5.server.app.adapters;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.executor.Executor;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.APIApplication;

/**
 * API Application to forward all messages to Executor instance.
 * Request commands are meant to be identical to App and Executor.
 */
@Slf4j
public class ExecutorAdapterApplication extends APIApplication {

    private final Executor target;

    public ExecutorAdapterApplication(Executor target) {
        this.target = target;
    }

    /**
     * Calls executor and if command not found passes control forward
     *
     * @param request request to be processed
     */
    @Override
    protected void apply(AppRequest request) {
        APICommand command = request.payload();
        log.debug("DBE: New request from {}: {}", request.session().getAddress(), request);

        APICommandResponse resp = target.execute(command);
        if (resp.isResolved()) {
            request.response()
                    .from(resp)
                    .send();
        }
    }
}
