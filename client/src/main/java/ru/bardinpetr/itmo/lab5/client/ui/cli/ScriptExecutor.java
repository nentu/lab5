package ru.bardinpetr.itmo.lab5.client.ui.cli;

import ru.bardinpetr.itmo.lab5.client.api.description.APICommandsDescriptionHolder;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.UICallableCommand;
import ru.bardinpetr.itmo.lab5.client.controller.registry.CommandRegistry;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.ConsolePrinter;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.ScriptRecursionController;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.ScriptException;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.ScriptRecursionRootException;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.common.io.FileIOController;
import ru.bardinpetr.itmo.lab5.common.io.exceptions.FileAccessException;

import java.util.List;

/**
 * Class for recursive parsing of nested script files
 */
public class ScriptExecutor {
    private final ScriptRecursionController recursionController;
    private final UICommandInvoker invoker;
    private final APICommandsDescriptionHolder descriptionHolder;
    private CommandRegistry commandRegistry = null;

    public ScriptExecutor(APICommandsDescriptionHolder descriptionHolder, UICommandInvoker invoker) {
        this.descriptionHolder = descriptionHolder;
        this.invoker = invoker;
        this.recursionController = new ScriptRecursionController();
    }

    /**
     * Set CommandRegistry to use
     *
     * @param commandRegistry CommandRegistry object from which commands should be executed
     */
    public void setRegistry(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    /**
     * Open and execute all commands in file with specified path.
     * If recursion occurred or script command was invalid, the exception is passed to first process() in the chain
     * meanwhile stopping the processing of all nested scripts, at root ScriptRecursionRootException should be handled
     *
     * @param path file path
     * @throws FileAccessException          if script could not be read
     * @throws ScriptException              if recursion occurs in nested scripts (not root) - should be passed to parent executor calls
     * @throws ScriptRecursionRootException if recursion occurs and the root of execution tree has been reached when going backwards - only this should be handled as the error
     */
    public void process(String path) throws FileAccessException, ScriptException {
        if (commandRegistry == null) throw new RuntimeException("No command registry");

        var isNormal = recursionController.enter(path);
        if (!isNormal)
            throw new ScriptException("Recursion detected at %s".formatted(path));

        FileIOController fileIOController = new FileIOController(path, false);

        UIReceiver uiReceiver = new CLIController(
                descriptionHolder,
                ConsolePrinter.getStub(),
                fileIOController.openReadStream(),
                false
        );

        var currentRegistry = commandRegistry.withUI(uiReceiver);
        while (uiReceiver.hasNextLine()) {
            var line = uiReceiver.nextLine();
            if (line == null) break;

            var userArgs = List.of(line.split("\\s+"));
            var command = (UICallableCommand) currentRegistry.getCommand(userArgs.get(0));
            if (command == null)
                throw new RuntimeException("Command not found");

            try {
                var successful = invoker.invoke(command, userArgs);
                if (!successful)
                    throw new ScriptException("failed");
            } catch (ScriptException ex) {
                recursionController.leave(path);
                if (recursionController.getDepth() == 0)
                    throw new ScriptRecursionRootException(ex.getMessage());
                throw ex;
            }
        }

        recursionController.leave(path);
    }
}
