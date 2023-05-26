package ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors;

/**
 * Exception for recursion errors of nested script processing
 * that is thrown only when the recursion ended and returned to the first script caller
 */
public class ScriptRecursionRootException extends RuntimeException {
    public ScriptRecursionRootException(String message) {
        super(message);
    }
}
