package ru.bardinpetr.itmo.lab5.models.commands.auth;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.AuthStrategy;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

/**
 * Command for performing login
 */
@Data
public class PasswordLoginCommand extends LoginCommand<DefaultAuthenticationCredentials> {

    public PasswordLoginCommand() {
        strategy = AuthStrategy.LOGIN_PASS;
    }

    @Override
    public Field<?>[] getInteractArgs() {
        return new Field[]{
                new Field<>("credentials", DefaultAuthenticationCredentials.class),
        };
    }

    @Override
    public String getType() {
        return "login";
    }
}
