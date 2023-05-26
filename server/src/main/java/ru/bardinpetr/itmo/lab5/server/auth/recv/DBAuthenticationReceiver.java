package ru.bardinpetr.itmo.lab5.server.auth.recv;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.models.commands.auth.LoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.RegisterCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthStrategy;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultLoginResponse;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.InvalidCredentialsException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.UserExistsException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.UserNotFoundException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.models.Authentication;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.recv.AuthenticationReceiver;
import ru.bardinpetr.itmo.lab5.server.auth.passwords.IPasswordController;
import ru.bardinpetr.itmo.lab5.server.auth.passwords.SHAPasswordController;
import ru.bardinpetr.itmo.lab5.server.db.dao.exception.OverLimitedUsername;
import ru.bardinpetr.itmo.lab5.server.db.dao.tables.UsersPGDAO;
import ru.bardinpetr.itmo.lab5.server.db.dto.UserDTO;

import java.util.Arrays;

@Slf4j
public class DBAuthenticationReceiver implements AuthenticationReceiver<DefaultAuthenticationCredentials, DefaultLoginResponse> {
    private final IPasswordController pc = new SHAPasswordController();
    private final UsersPGDAO usersDBDAO;

    public DBAuthenticationReceiver(UsersPGDAO usersDBDAO) {
        this.usersDBDAO = usersDBDAO;
    }

    @Override
    public Authentication authorize(DefaultAuthenticationCredentials request) {
        UserDTO authObj;
        try {
            authObj = usersDBDAO.selectByUsername(request.getUsername());
        } catch (OverLimitedUsername e) {
            return new Authentication(
                    Authentication.AuthenticationStatus.INVALID
            );
        }
        if (authObj == null) return new Authentication(
                Authentication.AuthenticationStatus.INVALID
        );

        var hashedPassword = pc.getHash(
                request.getPassword(),
                authObj.getSalt()
        );

        return new Authentication(
                Arrays.equals(authObj.getHashedPassword(), hashedPassword) ?
                        Authentication.AuthenticationStatus.NORMAL :
                        Authentication.AuthenticationStatus.INVALID,
                authObj.getId(),
                authObj.getUsername(),
                "user"
        );
    }

    @Override
    public DefaultLoginResponse login(LoginCommand command) throws UserNotFoundException, InvalidCredentialsException {
        if (!command.getStrategy().equals(AuthStrategy.LOGIN_PASS))
            throw new InvalidCredentialsException("Invalid strategy");

        DefaultAuthenticationCredentials creds = (DefaultAuthenticationCredentials) command.getCredentials();
        Authentication res = authorize(creds);

        if (res.getStatus() != Authentication.AuthenticationStatus.NORMAL)
            throw new UserNotFoundException();

        return new DefaultLoginResponse(creds.getUsername(), res.getUserHandle(), res.getRole());
    }

    @Override
    public DefaultLoginResponse register(RegisterCommand command) throws UserExistsException, InvalidCredentialsException {
        DefaultAuthenticationCredentials creds = command.getCredentials();

        try {
            if (usersDBDAO.selectByUsername(creds.getUsername()) != null)
                throw new UserExistsException();
        } catch (OverLimitedUsername e) {
            throw new UserExistsException();
        }

        Integer id;
        try {
            id = usersDBDAO.insert(
                    new UserDTO(
                            creds.getUsername(),
                            creds.getPassword()
                    )
            );
        } catch (Throwable e) {
            throw new InvalidCredentialsException(e);
        }

        return new DefaultLoginResponse(creds.getUsername(), id, "user");
    }

    @Override
    public DefaultLoginResponse requestIdentity(int userId) {
        UserDTO data;
        try {
            data = usersDBDAO.select(userId);
        } catch (Exception e) {
            return null;
        }
        return new DefaultLoginResponse(
                data.getUsername(),
                data.getId(),
                "user" // TODO get role
        );
    }
}
