package ru.bardinpetr.itmo.lab5.network.app.server.handlers.impl;

import ru.bardinpetr.itmo.lab5.network.app.server.interfaces.types.IRequestFilter;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.models.Authentication;

public class AuthenticatedFilter implements IRequestFilter {
    private static AuthenticatedFilter instance;

    public static AuthenticatedFilter getInstance() {
        if (instance == null)
            instance = new AuthenticatedFilter();
        return instance;
    }

    @Override
    public boolean filter(AppRequest request) {
        return request.session().getAuth().getStatus() == Authentication.AuthenticationStatus.NORMAL;
    }
}
