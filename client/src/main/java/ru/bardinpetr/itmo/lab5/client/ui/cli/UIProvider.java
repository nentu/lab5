package ru.bardinpetr.itmo.lab5.client.ui.cli;

public class UIProvider {

    private static CLIController instance;

    public static void setInstance(CLIController uiController) {
        instance = uiController;
    }

    public static CLIController get() {
        return instance;
    }
}
