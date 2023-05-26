package ru.bardinpetr.itmo.lab5.client.controller.common;

import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;

import java.util.Map;

/**
 * Abstract command for execution on client side
 */
public abstract class AbstractLocalCommand {

    /**
     * Execute command
     *
     * @param cmdName command handler/name - used for commands with multiple realizations
     * @param args    arguments of command parsed according to fields
     * @return response of execution
     */
    public abstract ClientCommandResponse<?> execute(String cmdName, Map<String, Object> args);
}
