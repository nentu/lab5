package ru.bardinpetr.itmo.lab5.mainclient.local.controller.auth.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.auth.PasswordLoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;

/**
 * Login implementation of authentication command
 */
public class LoginLocalCommand<T extends AuthenticationCredentials> extends AuthLocalCommand<T> {

    public LoginLocalCommand(APIClientConnector api, UIReceiver ui, ICredentialsStorage<T> credentialsStorage) {
        super(
                new PasswordLoginCommand(),
                api, ui, credentialsStorage
        );
    }

    @Override
    protected void onSuccess(AuthenticationCredentials credentials, APICommandResponse resp) {
        this.credentialsStorage.setCredentials((T) credentials);
    }

    @Override
    protected String getPrompt() {
        return "Please login with username and password";
    }
}
