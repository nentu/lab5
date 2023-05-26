package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.recv;

import ru.bardinpetr.itmo.lab5.models.commands.auth.LoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.RegisterCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultLoginResponse;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.LoginResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.InvalidCredentialsException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.UserExistsException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.UserNotFoundException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.models.Authentication;

/**
 * Service for validating of authentication requests
 *
 * @param <C> type of AuthenticationCredentials used
 * @param <R> type of RegistrationResponse used
 */
public interface AuthenticationReceiver<C extends AuthenticationCredentials, R extends LoginResponse> {

    Authentication authorize(C request);

    R login(LoginCommand request) throws UserNotFoundException, InvalidCredentialsException;

    R register(RegisterCommand command) throws UserExistsException, InvalidCredentialsException;

    DefaultLoginResponse requestIdentity(int userId);
}

