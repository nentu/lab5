package ru.bardinpetr.itmo.lab5.models.commands.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthStrategy;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;

/**
 * Command for performing login
 */
@Data
@RequiredArgsConstructor
public abstract class LoginCommand<T extends AuthenticationCredentials> extends AuthCommand<T> {
    protected AuthStrategy strategy = AuthStrategy.LOGIN_PASS;
}
