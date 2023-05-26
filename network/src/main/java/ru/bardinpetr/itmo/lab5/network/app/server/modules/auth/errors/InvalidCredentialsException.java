package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors;

public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}
