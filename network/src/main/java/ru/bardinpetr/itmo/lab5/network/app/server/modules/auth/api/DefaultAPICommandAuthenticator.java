package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.api;

import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;


/**
 * Helper class for injecting authentication credentials into APICommand
 */
public final class DefaultAPICommandAuthenticator extends APICommandAuthenticator<DefaultAuthenticationCredentials> {
    private static DefaultAPICommandAuthenticator instance;

    private DefaultAPICommandAuthenticator() {
    }

    public static DefaultAPICommandAuthenticator getInstance() {
        if (instance == null)
            instance = new DefaultAPICommandAuthenticator();
        return instance;
    }
}
