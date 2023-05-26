package ru.bardinpetr.itmo.lab5.common.error;

public class APIClientException extends Throwable {
    public APIClientException(String message) {
        super(message);
    }

    public APIClientException(Throwable cause) {
        super(cause.getMessage());
    }
}
