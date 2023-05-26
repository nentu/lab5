package ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.error;

public class NotOwnedException extends RuntimeException {
    public NotOwnedException(String user) {
        super("Object is not owned by user %s".formatted(user));
    }
    public NotOwnedException() {
        super("Object is not owned by you");
    }
}
