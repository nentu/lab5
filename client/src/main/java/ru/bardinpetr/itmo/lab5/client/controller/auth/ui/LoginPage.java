package ru.bardinpetr.itmo.lab5.client.controller.auth.ui;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.controller.auth.api.StoredJWTCredentials;
import ru.bardinpetr.itmo.lab5.client.controller.auth.commands.AuthLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.auth.commands.JWTLoginLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.auth.commands.JWTRegisterLocalCommand;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;

import java.util.Map;

/**
 * Service for requesting user to authenticate before running main application
 */
public class LoginPage<T extends AuthenticationCredentials> {
    private static LoginPage<AuthenticationCredentials> instance;
    private final Runnable callback;
    private final UIReceiver ui;
    private final ICredentialsStorage<StoredJWTCredentials> storage;
    private final APIClientConnector api;
    private final JWTRegisterLocalCommand registerCmd;
    private final JWTLoginLocalCommand loginCmd;

    public LoginPage(APIClientConnector api, UIReceiver ui, ICredentialsStorage<StoredJWTCredentials> credentialsStorage, Runnable onLogin) {
        this.callback = onLogin;
        this.ui = ui;
        this.api = api;

        this.storage = credentialsStorage;

        this.loginCmd = new JWTLoginLocalCommand(api, ui, storage);
        this.registerCmd = new JWTRegisterLocalCommand(api, ui, storage);
    }

    public void run() {
        if (storage.getCredentials() != null) {
            callback.run();
            return;
        }

        AuthLocalCommand<StoredJWTCredentials> selectedCommand;
        while (true) {
            ui.display("You need to authenticate. Type L for login or R for registration");
            var req = ui.nextLine();
            if (req.equals("R")) {
                selectedCommand = registerCmd;
                break;
            }
            if (req.equals("L")) {
                selectedCommand = loginCmd;
                break;
            }
            ui.display("Invalid choice");
        }

        var res = selectedCommand.execute(null, Map.of());
        if (res.isSuccess()) {
            callback.run();
        } else {
            run();
        }
    }
}
