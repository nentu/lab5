package ru.bardinpetr.itmo.lab5.client.controller.auth.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.controller.auth.api.StoredJWTCredentials;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.auth.AuthCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTLoginResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;

public class JWTRegisterLocalCommand extends RegisterLocalCommand<StoredJWTCredentials> {
    public JWTRegisterLocalCommand(APIClientConnector api, UIReceiver ui, ICredentialsStorage<StoredJWTCredentials> credentialsStorage) {
        super(api, ui, credentialsStorage);
    }

    @Override
    protected void onSuccess(AuthenticationCredentials credentials, APICommandResponse resp) {
        var loginResponse = ((AuthCommand.AuthCommandResponse) resp).getData();
        credentialsStorage.setCredentials(new StoredJWTCredentials((JWTLoginResponse) loginResponse));
    }
}
