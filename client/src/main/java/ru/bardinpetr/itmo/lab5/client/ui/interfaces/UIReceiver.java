package ru.bardinpetr.itmo.lab5.client.ui.interfaces;

/**
 * Interface for communication with UI for entering information and displaying data
 */
public interface UIReceiver extends UIInputReceiver {
    /**
     * print text on screen
     */
    void display(String text);

    /**
     * for CLI print an invitation for command enter
     */
    void interactSuggestion();

    /**
     * Print successful message
     */
    void ok();

    /**
     * Print successful message mentioning specific command
     *
     * @param cmd command name
     */
    void ok(String cmd);

    /**
     * print error message
     *
     * @param message text
     */
    void error(String message);

    InteractSpecialSymbols interactSpecial();
}
