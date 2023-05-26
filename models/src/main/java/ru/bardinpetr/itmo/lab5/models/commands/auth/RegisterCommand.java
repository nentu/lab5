package ru.bardinpetr.itmo.lab5.models.commands.auth;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

@Data
public class RegisterCommand extends AuthCommand<DefaultAuthenticationCredentials> {

    @Override
    public Field<?>[] getInteractArgs() {
        return new Field[]{
                new Field<>("credentials", DefaultAuthenticationCredentials.class),
        };
    }

    @Override
    public String getType() {
        return "register";
    }
}
