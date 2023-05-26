package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {
    private AuthenticationStatus status = AuthenticationStatus.GUEST;
    private Integer userHandle = null;
    private String userName = null;
    private String role = null;

    public Authentication(AuthenticationStatus status) {
        this.status = status;
    }

    public static Authentication invalid() {
        return new Authentication(AuthenticationStatus.INVALID);
    }

    public static Authentication ok(Integer userHandle, String userName, String role) {
        return new Authentication(AuthenticationStatus.NORMAL, userHandle, userName, role);
    }

    public enum AuthenticationStatus {
        GUEST,
        INVALID,
        NORMAL
    }
}
