package ru.bardinpetr.itmo.lab5.models.commands.auth;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthStrategy;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTAuthenticationCredentials;

/**
 * Command for performing login
 */
@Data
public class RefreshLoginCommand extends LoginCommand<JWTAuthenticationCredentials> {
    public RefreshLoginCommand() {
        strategy = AuthStrategy.REFRESH_TOKEN;
    }

    @Override
    public String getType() {
        return "login";
    }
}
