package ru.bardinpetr.itmo.lab5.client.api.auth;

import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;

/**
 * Interface for local API credentials storage services.
 *
 * @param <C> credentials type
 */
public interface ICredentialsStorage<C extends AuthenticationCredentials> {
    /**
     * Remove credentials from storage
     */
    void clear();

    /**
     * Get stored credentials
     *
     * @return credentials or null if not stored
     */
    C getCredentials();

    /**
     * Store credentials
     */
    void setCredentials(C newCredentials);
}
