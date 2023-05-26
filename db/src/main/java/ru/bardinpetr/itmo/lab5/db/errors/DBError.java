package ru.bardinpetr.itmo.lab5.db.errors;

public class DBError extends RuntimeException {
    public DBError(String message) {
        super(message);
    }

    public DBError(Throwable cause) {
        super(cause);
    }
}
