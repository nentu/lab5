package ru.bardinpetr.itmo.lab5.client.ui.cli;

import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.UICallableCommand;
import ru.bardinpetr.itmo.lab5.client.controller.registry.CommandRegistry;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;

import java.util.List;

/**
 * Class for console UI
 */
public class Interpreter {
    private final UIReceiver uiReceiver;
    private final CommandRegistry registryCommand;
    private final UICommandInvoker invoker;

    public Interpreter(CommandRegistry registryCommand, UIReceiver uiReceiver, UICommandInvoker invoker) {
        this.registryCommand = registryCommand;
        this.uiReceiver = uiReceiver;
        this.invoker = invoker;
    }

    /**
     * Get commands from CLI and execute them
     */
    public void run() {
        uiReceiver.interactSuggestion();
        while (uiReceiver.hasNextLine()) {
            var line = uiReceiver.nextLine();
            if (line == null) System.exit(0);

            var userArgs = List.of(line.split("\\s+"));
            var command = (UICallableCommand) registryCommand.getCommand(userArgs.get(0));
            if (command == null) {
                uiReceiver.display("Command not found");
                uiReceiver.interactSuggestion();
                continue;
            }

            invoker.invoke(command, userArgs);
            uiReceiver.interactSuggestion();
        }
    }
}
