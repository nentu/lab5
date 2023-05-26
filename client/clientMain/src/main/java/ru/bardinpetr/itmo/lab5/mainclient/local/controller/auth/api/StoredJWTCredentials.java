package ru.bardinpetr.itmo.lab5.mainclient.local.controller.auth.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTInfo;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTLoginResponse;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredJWTCredentials extends AuthenticationCredentials {
    private JWTInfo authToken;
    private JWTInfo refreshToken;

    public StoredJWTCredentials(JWTLoginResponse source) {
        this(source.getAuthToken(), source.getRefreshToken());
    }

    public String safeIdentifier() {
        return authToken.token();
    }
}
