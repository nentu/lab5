package ru.bardinpetr.itmo.lab5.server.app.modules.script;

import ru.bardinpetr.itmo.lab5.common.executor.Executor;
import ru.bardinpetr.itmo.lab5.models.commands.api.ExecuteScriptCommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;

/**
 * Runs command batches via specified target executor for ExecuteScriptCommand
 */
public class ScriptExecutor extends Executor {

    private final Executor targetExecutor;


    /**
     * @param target the executor to which command run will be delegated
     */
    public ScriptExecutor(Executor target) {
        registerOperation(ExecuteScriptCommand.class, this::processScript);
        this.targetExecutor = target;
    }

    /**
     * Take commands from input message and pass to executor
     *
     * @param req call script command
     * @return response with list of responses for executed commands
     */
    private APICommandResponse processScript(ExecuteScriptCommand req) {
        var resp = req.createResponse();
        var commands = req.getCommands();
        if (commands.isEmpty()) throw new RuntimeException("No commands");
        resp.setResult(targetExecutor.executeBatch(commands));
        return resp;
    }
}
