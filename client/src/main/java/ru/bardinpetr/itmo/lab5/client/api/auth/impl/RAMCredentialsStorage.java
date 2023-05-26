package ru.bardinpetr.itmo.lab5.client.api.auth.impl;

import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;

/**
 * Service for storing authentication credentials locally in RAM.
 */
public class RAMCredentialsStorage<C extends AuthenticationCredentials> implements ICredentialsStorage<C> {

    private C creds;

    @Override
    public void clear() {
        creds = null;
    }

    @Override
    public C getCredentials() {
        return creds;
    }

    @Override
    public void setCredentials(C newCredentials) {
        creds = newCredentials;
    }
}
