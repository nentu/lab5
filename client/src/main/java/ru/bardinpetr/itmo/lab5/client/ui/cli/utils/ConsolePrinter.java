package ru.bardinpetr.itmo.lab5.client.ui.cli.utils;

/**
 * Class for printing in console
 */
public class ConsolePrinter {
    public static ConsolePrinter getStub() {
        return new ConsolePrinter() {
            @Override
            public void display(String text) {
            }

            @Override
            public void displayInLine(String text) {
            }
        };
    }

    public void display(String text) {
        System.out.println(text);
    }

    public void displayInLine(String text) {
        System.out.print(text);
    }
}
