package ru.bardinpetr.itmo.lab5.db.errors;

public class DBBackendIOException extends Exception {
    public DBBackendIOException() {
    }

    public DBBackendIOException(String message) {
        super(message);
    }

    public DBBackendIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBBackendIOException(Throwable cause) {
        super(cause);
    }
}
