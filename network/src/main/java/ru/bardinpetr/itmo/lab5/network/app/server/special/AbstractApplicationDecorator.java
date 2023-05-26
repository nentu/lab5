package ru.bardinpetr.itmo.lab5.network.app.server.special;

import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

/**
 * Redirects requests
 */
public class AbstractApplicationDecorator extends AbstractApplication {

    protected final AbstractApplication decoratee;

    public AbstractApplicationDecorator(AbstractApplication decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    protected void apply(AppRequest request) {
        decoratee.process(request);
    }
}

