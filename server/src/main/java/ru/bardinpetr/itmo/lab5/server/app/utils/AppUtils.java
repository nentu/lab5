package ru.bardinpetr.itmo.lab5.server.app.utils;

import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

public class AppUtils {

    public static Integer extractUser(AppRequest request) {
        var auth = request.session().getAuth();
        return auth != null ? auth.getUserHandle() : null;
    }
}
