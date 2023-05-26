package ru.bardinpetr.itmo.lab5.network.app.server.handlers;

import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.network.app.server.interfaces.types.IRequestFilter;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

import java.util.HashSet;
import java.util.Set;

public record RequestHandler(IApplicationCommandHandler handler, Set<IRequestFilter> filters) {
    public RequestHandler(IApplicationCommandHandler handler) {
        this(handler, new HashSet<>());
    }

    public void handle(AppRequest appRequest) {
        for (var filter : filters) {
            if (filter != null && !filter.filter(appRequest)) {
                appRequest.response().status(APIResponseStatus.AUTH_ERROR).send();
                return;
            }
        }
        handler.handle(appRequest);
    }
}
