package ru.bardinpetr.itmo.lab5.client.controller.auth.api;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.AuthenticatedConnectorDecorator;
import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.controller.auth.ui.LoginPage;
import ru.bardinpetr.itmo.lab5.client.ui.cli.UIProvider;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.auth.AuthCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.RefreshLoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthStrategy;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTInfo;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTLoginResponse;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.api.APICommandAuthenticator;

import java.time.Duration;
import java.time.ZonedDateTime;

public class JWTAuthConnector extends AuthenticatedConnectorDecorator<JWTAuthenticationCredentials, StoredJWTCredentials> {
    public JWTAuthConnector(APICommandAuthenticator<JWTAuthenticationCredentials> commandAuthenticator, ICredentialsStorage<StoredJWTCredentials> storage, APIClientConnector decoratee) {
        super(commandAuthenticator, storage, decoratee);
    }

    @Override
    protected JWTAuthenticationCredentials prepareCredentials(StoredJWTCredentials storageCredentials) {
        if (storageCredentials == null || storageCredentials.getAuthToken() == null)
            return null;

        return new JWTAuthenticationCredentials(
                storageCredentials.getAuthToken().token()
        );
    }

    @Override
    public APICommandResponse call(APICommand cmd) throws APIClientException {
        var creds = storage.getCredentials();
        if (creds == null)
            return decoratee.call(cmd);

        var auth = creds.getAuthToken();
        var now = ZonedDateTime.now();

        if (Duration.between(now, auth.expiration()).getSeconds() < 5)
            doRefreshToken(creds.getRefreshToken());

        var authed = authenticator.authenticate(cmd, prepareCredentials(storage.getCredentials()));
        return decoratee.call(authed);
    }

    private void doRefreshToken(JWTInfo refresh) {
        var ui = UIProvider.get();

        if (refresh.expiration().isBefore(ZonedDateTime.now())) {
            doPasswordLogin();
            return;
        }

        var refreshCmd = new RefreshLoginCommand();
        refreshCmd.setStrategy(AuthStrategy.REFRESH_TOKEN);
        refreshCmd.setCredentials(new JWTAuthenticationCredentials(refresh.token()));
        AuthCommand.AuthCommandResponse result;
        try {
            result = (AuthCommand.AuthCommandResponse) decoratee.call(refreshCmd);
            if (!result.isSuccess()) {
                if (ui != null)
                    ui.error("Unable to use refresh token. Retrying");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                doRefreshToken(refresh);
                return;
            }
        } catch (Throwable e) {
            doPasswordLogin();
            return;
        }

        var creds = new StoredJWTCredentials((JWTLoginResponse) result.getData());
        if (ui != null)
            ui.display("Updated token. Exp for access: %s, for refresh %s".formatted(creds.getAuthToken().expiration(), creds.getRefreshToken().expiration()));
        storage.setCredentials(creds);
    }

    private void doPasswordLogin() {
        storage.setCredentials(null);

        new LoginPage<>(
                decoratee,
                UIProvider.get(),
                storage,
                () -> {
                }
        ).run();
    }
}
