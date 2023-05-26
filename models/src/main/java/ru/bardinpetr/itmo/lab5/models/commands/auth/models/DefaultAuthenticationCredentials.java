package ru.bardinpetr.itmo.lab5.models.commands.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.data.annotation.InteractText;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultAuthenticationCredentials extends AuthenticationCredentials {
    @InteractText("Enter username")
    private String username;

    @InteractText("Enter password")
    private String password;
}
