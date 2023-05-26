package ru.bardinpetr.itmo.lab5.client.ui.cli;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.bardinpetr.itmo.lab5.client.api.description.APICommandsDescriptionHolder;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.ConsolePrinter;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.ObjectScanner;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.NotRepeatableException;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.ParserException;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.InteractSpecialSymbols;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main CLI interaction object
 */
public class CLIController implements UIReceiver {
    private final ConsolePrinter printer;
    private final ObjectScanner objectScanner;
    private final Scanner scanner;
    private final boolean isRepeatable;


    public CLIController(APICommandsDescriptionHolder descriptionHolder, ConsolePrinter printer, InputStream inputStream, boolean isRepeatable) {
        this.scanner = new Scanner(inputStream);
        this.objectScanner = new ObjectScanner(descriptionHolder, printer, scanner);
        this.printer = printer;
        this.isRepeatable = isRepeatable;
    }

    /**
     * Fill object from UI
     *
     * @param target        class of object to request from user
     * @param defaultObject object from which to take field default values
     * @param <T>           object type
     * @return built object
     */
    @Override
    public <T> T fill(Class<T> target, T defaultObject, List<String> blacklist) {
        try {
            var resp = objectScanner.scan(target, defaultObject, blacklist);
            if ((!isRepeatable) & resp.countOfRepeat > 0) throw new NotRepeatableException();
            return resp.getObject();
        } catch (ParserException e) {
            throw new RuntimeException("Parse exception: " + e.getMessage());
        }
    }

    /**
     * @return true if there is next line to read from stream
     */
    @Override
    public boolean hasNextLine() {
        try {
            return scanner.hasNextLine();
        } catch (IllegalStateException ignored) {
            return false;
        }
    }

    /**
     * read line from stream.
     *
     * @return line or null if stream closed
     */
    @Override
    public String nextLine() {
        try {
            return scanner.nextLine();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * print text on screen
     */
    @Override
    public void display(String text) {
        printer.display(text);
    }

    /**
     * for CLI print an invitation for command enter
     */
    @Override
    public void interactSuggestion() {
        System.out.print("> ");
    }

    /**
     * Print successful message
     */
    @Override
    public void ok() {
        printer.display("Command successful");
    }

    /**
     * Print successful message mentioning specific command
     *
     * @param cmd command name
     */
    @Override
    public void ok(String cmd) {
        printer.display("Command %s successful".formatted(cmd));
    }

    /**
     * print error message
     *
     * @param message text
     */
    @Override
    public void error(String message) {
        printer.display("Error: %s".formatted(message));
    }

    public InteractSpecialSymbols interactSpecial() {
        InteractSpecialSymbols status = InteractSpecialSymbols.EXIT;
        Terminal terminal;
        try {
            terminal = TerminalBuilder.terminal();
        } catch (IOException ignored) {
            return InteractSpecialSymbols.EXIT;
        }
        terminal.enterRawMode();
        var reader = terminal.reader();

        List<Integer> sequence = null;
        while (true) {
            int symbol;
            try {
                symbol = reader.read(10);
            } catch (IOException ignored) {
                break;
            }
            if (symbol <= 0) continue;
            if (symbol == 113) break;
            if (symbol == 27)
                sequence = new ArrayList<>();
            if (sequence != null) {
                sequence.add(symbol);
                if (sequence.equals(List.of(27, 91, 65))) {
                    status = InteractSpecialSymbols.UP;
                    break;
                } else if (sequence.equals(List.of(27, 91, 66))) {
                    status = InteractSpecialSymbols.DOWN;
                    break;
                }
            }
        }

        try {
            reader.close();
            terminal.close();
        } catch (IOException ignored) {
        }

        return status;
    }
}
