package ru.bardinpetr.itmo.lab5.client.controller.common.handlers;

import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;

import java.util.List;

/**
 * Local command that can be executed from UI
 */
public interface UICallableCommand {

    /**
     * Call command from CLI. Arguments should be parsed the way to call execute() then
     *
     * @param args list of arguments including command name as args[0]
     * @return result of execution
     */
    ClientCommandResponse<? extends UserPrintableAPICommandResponse> executeWithArgs(List<String> args);
}
