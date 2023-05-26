package ru.bardinpetr.itmo.lab5.network.app.server.special.impl;

import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.interfaces.types.IRequestFilter;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.app.server.special.AbstractApplicationDecorator;

public class FilteredApplication extends AbstractApplicationDecorator {

    private final IRequestFilter filter;

    public FilteredApplication(AbstractApplication decoratee, IRequestFilter filter) {
        super(decoratee);
        this.filter = filter;
    }

    @Override
    public boolean filter(AppRequest request) {
        return filter == null || filter.filter(request);
    }
}
