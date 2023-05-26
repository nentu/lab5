package ru.bardinpetr.itmo.lab5.client.ui.cli;

import ru.bardinpetr.itmo.lab5.client.controller.commands.RepeatLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.UICallableCommand;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.InvocationHistoryItem;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.ScriptException;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for running local commands in UI and parsing their return values and handle errors
 */
public class UICommandInvoker {

    private final UIReceiver screenUIReceiver;
    private final List<InvocationHistoryItem> commandHistory = new ArrayList<>();

    /**
     * @param screenUIReceiver UIReceiver for outputting results and errors to user
     */
    public UICommandInvoker(UIReceiver screenUIReceiver) {
        this.screenUIReceiver = screenUIReceiver;
    }

    /**
     * Call command and catch all exceptions printing them as CommandResponse.
     *
     * @param command command object
     * @param args    command argument from CLI
     * @return true if command succeeded
     * @throws ScriptException this exception is passed to the root of nested scripts and only there should be handled
     */
    public boolean invoke(UICallableCommand command, List<String> args) throws ScriptException {
        ClientCommandResponse<? extends UserPrintableAPICommandResponse> resp;
        try {
            resp = command.executeWithArgs(args);
        } catch (ScriptException ex) {
            throw ex; // Should be handled by ScriptExecutor and ScriptLocalCommand
        } catch (Exception ex) {
            resp = ClientCommandResponse.error(ex.getMessage());
        } finally {
            if (command.getClass() != RepeatLocalCommand.class)
                commandHistory.add(new InvocationHistoryItem(command, args));
        }
        if (resp == null) resp = ClientCommandResponse.ok();

        print(args.size() > 0 ? args.get(0) : null, resp);

        return resp.isSuccess();
    }

    /**
     * Print to ui command's response as payload, text message or ok/error
     *
     * @param caller name of called function or null to ignore
     * @param result response of command
     */
    public void print(String caller, ClientCommandResponse<? extends UserPrintableAPICommandResponse> result) {
        var payload = result.payload();
        var textualResponse = result.message();

        var callerText = caller == null ? "" : "command %s: ".formatted(caller);
        textualResponse = textualResponse == null ? "" : textualResponse;

        if (!result.isSuccess()) {
            if (payload != null)
                screenUIReceiver.error(callerText + payload.getUserMessage());
            else
                screenUIReceiver.error(callerText + textualResponse);
            return;
        }

        if (payload != null) {
            screenUIReceiver.display(payload.getUserMessage());
        } else if (!textualResponse.isEmpty()) {
            screenUIReceiver.display(textualResponse);
        } else if (caller != null) {
            screenUIReceiver.ok(caller);
        } else {
            screenUIReceiver.ok();
        }
    }

    /**
     * Call last invoked command and catch all exceptions printing them as CommandResponse.
     *
     * @return true if command succeeded
     * @throws ScriptException  this exception is passed to the root of nested scripts and only there should be handled
     * @throws RuntimeException if
     */
    public boolean invokeLast() {
        if (commandHistory.isEmpty())
            throw new RuntimeException("No commands in history");

        var cmd = commandHistory.get(commandHistory.size() - 1);
        return invoke(cmd.command(), cmd.args());
    }
}
