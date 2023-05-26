package ru.bardinpetr.itmo.lab5.network.app.server.special.impl;

import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

/**
 * Application for terminating any requests that was not terminated by other applications or requests that are invalid
 */
public class ErrorHandlingApplication extends APIApplication {
    @Override
    public void apply(AppRequest request) {
        var resp = request.response();
        resp
                .message("Method not available")
                .status(APIResponseStatus.NOT_FOUND)
                .send();
    }

    /**
     * Allows only not terminated requests
     */
    @Override
    public boolean filter(AppRequest req) {
        return !req.isTerminated();
    }
}
