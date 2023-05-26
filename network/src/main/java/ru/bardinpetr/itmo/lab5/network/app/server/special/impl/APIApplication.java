package ru.bardinpetr.itmo.lab5.network.app.server.special.impl;

import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

/**
 * Base application for writing API handlers.
 * Pre-configured to answer valid requests.
 */
public class APIApplication extends AbstractApplication {

    @Override
    public boolean filter(AppRequest req) {
        return req.status() == AppRequest.ReqStatus.NORMAL;
    }
}
