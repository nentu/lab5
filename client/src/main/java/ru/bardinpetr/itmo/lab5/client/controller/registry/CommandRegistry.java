package ru.bardinpetr.itmo.lab5.client.controller.registry;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.controller.common.AbstractLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.UILocalCommand;
import ru.bardinpetr.itmo.lab5.client.ui.cli.ScriptExecutor;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing prepared client-side commands with their handlers-name to invoke via
 */
public abstract class CommandRegistry {
    protected final APIClientConnector api;
    protected final ScriptExecutor scriptExecutor;
    private final Map<String, AbstractLocalCommand> commandMap = new HashMap<>();

    public CommandRegistry(APIClientConnector api, ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
        this.api = api;

        scriptExecutor.setRegistry(this);
    }

    /**
     * Create same command invoker with changed ui dependency
     *
     * @param otherUI ui receiver to be used
     * @return this command registry with all commands and changed ui dependency
     */
    public abstract CommandRegistry withUI(UIReceiver otherUI);

    /**
     * Register command for all name taken from APICommand set
     *
     * @param apiCommands APICommand collection to take names from
     * @param command     target command
     */
    protected void registerFromAPI(Collection<UserAPICommand> apiCommands, AbstractLocalCommand command) {
        apiCommands.forEach(i -> register(i.getType(), command));
    }

    /**
     * Register command by its name
     *
     * @param command target command
     */
    protected void register(UILocalCommand command) {
        register(command.getExternalName(), command);
    }

    /**
     * Map command to its name.
     *
     * @param name    user input command name
     * @param command command object
     */
    protected void register(String name, AbstractLocalCommand command) {
        commandMap.put(name, command);
    }

    /**
     * Maps command object to a set of names
     *
     * @param names   collection on command names
     * @param command command object
     */
    protected void register(Collection<String> names, AbstractLocalCommand command) {
        names.forEach(i -> register(i, command));
    }

    /**
     * Retrieve command preconfigured object for name
     *
     * @param name user input command name
     * @return command
     */
    public AbstractLocalCommand getCommand(String name) {
        return commandMap.get(name);
    }
}
