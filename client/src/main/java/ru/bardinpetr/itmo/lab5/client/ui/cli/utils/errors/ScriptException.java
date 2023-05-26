package ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors;

/**
 * Exception for recursion errors of nested script processing
 */
public class ScriptException extends RuntimeException {
    public ScriptException(String message) {
        super(message);
    }
}
