package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt;

import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultLoginResponse;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTLoginResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.api.APICommandAuthenticator;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.AuthenticationApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.storage.JWTKeyProvider;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.recv.AuthenticationReceiver;

public class JWTAuthenticationApplication extends AuthenticationApplication<JWTAuthenticationCredentials, JWTLoginResponse> {
    public JWTAuthenticationApplication(APICommandAuthenticator<JWTAuthenticationCredentials> commandAuthenticator, AuthenticationReceiver<DefaultAuthenticationCredentials, DefaultLoginResponse> authenticationReceiver, JWTKeyProvider keyProvider) {
        super(
                commandAuthenticator,
                new JWTAuthenticationReceiverAdapter(authenticationReceiver, keyProvider)
        );
    }
}
