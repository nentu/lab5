package ru.bardinpetr.itmo.lab5.client.api.error;

public class APIUIException extends RuntimeException {
    public APIUIException(Throwable cause) {
        super(cause.getMessage());
    }
}
