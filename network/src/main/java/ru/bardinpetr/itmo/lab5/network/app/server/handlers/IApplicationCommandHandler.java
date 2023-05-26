package ru.bardinpetr.itmo.lab5.network.app.server.handlers;

import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

/**
 * handler for api commands
 */
public interface IApplicationCommandHandler {
    void handle(AppRequest request);
}
