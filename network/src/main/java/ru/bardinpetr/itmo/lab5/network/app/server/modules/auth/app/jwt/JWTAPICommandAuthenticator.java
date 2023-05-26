package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt;

import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.api.APICommandAuthenticator;

public class JWTAPICommandAuthenticator extends APICommandAuthenticator<JWTAuthenticationCredentials> {
    private static JWTAPICommandAuthenticator instance;

    private JWTAPICommandAuthenticator() {
    }

    public static JWTAPICommandAuthenticator getInstance() {
        if (instance == null)
            instance = new JWTAPICommandAuthenticator();
        return instance;
    }
}
