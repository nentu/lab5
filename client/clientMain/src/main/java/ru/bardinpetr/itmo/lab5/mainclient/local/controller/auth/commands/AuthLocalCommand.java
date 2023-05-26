package ru.bardinpetr.itmo.lab5.mainclient.local.controller.auth.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.auth.AuthCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;

import java.util.Map;

/**
 * Command for processing nested scripts
 */
public abstract class AuthLocalCommand<T extends AuthenticationCredentials> extends APIUILocalCommand {

    protected final ICredentialsStorage<T> credentialsStorage;
    private final AuthCommand<?> baseCommand;

    public AuthLocalCommand(AuthCommand<?> baseCommand, APIClientConnector api, UIReceiver ui, ICredentialsStorage<T> credentialsStorage) {
        super(api, ui, null);
        this.credentialsStorage = credentialsStorage;
        this.baseCommand = baseCommand;
    }

    public final String getExternalName() {
        return null;
    }

    @Override
    protected final AuthCommand<?> retrieveAPICommand(String name) {
        return baseCommand;
    }

    /**
     * Method to be called if authentication procedure succeeded
     *
     * @param credentials source credentials with request succeeded
     * @param resp        origin response on APICommand
     */
    protected abstract void onSuccess(AuthenticationCredentials credentials, APICommandResponse resp);

    /**
     * Method returning user prompt before auth data requested
     */
    protected abstract String getPrompt();

    @Override
    public ClientCommandResponse<APICommandResponse> execute(String cmdName, Map<String, Object> args) {
        uiReceiver.display(getPrompt());

        AuthCommand<?> cmd = (AuthCommand<?>) prepareAPIMessage(cmdName, args);

        try {
            var resp = apiClientReceiver.call(cmd);

            if (resp.isSuccess()) {
                uiReceiver.display("Authentication successful!");
                onSuccess(cmd.getCredentials(), resp);
                return ClientCommandResponse.ok();
            }

            if (resp.getStatus() == APIResponseStatus.SERVER_ERROR) {
                uiReceiver.error("Server error: %s".formatted(resp.getUserMessage()));
            } else {
                uiReceiver.display("Invalid auth data");
            }
        } catch (Throwable ex) {
            uiReceiver.display("Failed to authenticate: %s".formatted(ex.getMessage()));
        }

        return ClientCommandResponse.error("Authentication failed");
    }
}
