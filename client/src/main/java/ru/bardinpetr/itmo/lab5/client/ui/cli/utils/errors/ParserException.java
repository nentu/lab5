package ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors;

/**
 * Expression in parsing commands
 */
public class ParserException extends Exception {
    public ParserException(String msg) {
        super(msg);
    }
}
