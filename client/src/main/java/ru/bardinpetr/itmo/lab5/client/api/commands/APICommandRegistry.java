package ru.bardinpetr.itmo.lab5.client.api.commands;

import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;

import java.util.HashMap;
import java.util.List;

/**
 * Class for storing information on available APICommand
 */
public class APICommandRegistry {

    private final HashMap<String, UserAPICommand> map = new HashMap<>();
    private final List<UserAPICommand> cmdList;

    public APICommandRegistry(List<UserAPICommand> cmdList) {
        this.cmdList = cmdList;
        cmdList.forEach(cmd -> map.put(cmd.getType(), cmd));
    }

    /**
     * Get API command object for name
     *
     * @param name command name
     * @return command object
     */
    public UserAPICommand getCommand(String name) {
        return map.get(name);
    }

    public List<UserAPICommand> getCommands() {
        return cmdList;
    }
}
