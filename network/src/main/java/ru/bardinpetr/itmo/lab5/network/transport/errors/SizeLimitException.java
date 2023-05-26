package ru.bardinpetr.itmo.lab5.network.transport.errors;

public class SizeLimitException extends RuntimeException {
    public SizeLimitException(String e) {
        super("Maximum size exceeded: " + e);
    }
}
