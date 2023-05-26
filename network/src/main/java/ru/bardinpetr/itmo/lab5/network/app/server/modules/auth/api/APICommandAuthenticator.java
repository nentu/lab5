package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.api;

import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;


/**
 * Helper class for injecting authentication credentials into APICommand
 */
public class APICommandAuthenticator<C extends AuthenticationCredentials> {
    public static final String AUTH_HEADER = "Authentication";

    protected APICommandAuthenticator() {
    }

    /**
     * Adds authentication header to existing API command
     *
     * @param decoratee command to authenticate
     * @param authData  credentials or null to leave command unmodified
     * @return command with headers set
     */
    public APICommand authenticate(APICommand decoratee, C authData) {
        if (authData == null) return decoratee;

        decoratee.getHeader().put(
                AUTH_HEADER,
                authData
        );
        return decoratee;
    }

    /**
     * Retrieve credentials from command processed by authenticate()
     *
     * @param command command with AUTH_HEADER set
     * @return credentials or null if header is not present
     */
    public C extractAuth(APICommand command) {
        try {
            return (C) command.getHeader().get(AUTH_HEADER);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
