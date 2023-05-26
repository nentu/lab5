package ru.bardinpetr.itmo.lab5.client.controller.commands;

import ru.bardinpetr.itmo.lab5.client.controller.common.UILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.ui.cli.UICommandInvoker;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;

import java.util.Map;


public class RepeatLocalCommand extends UILocalCommand {

    private final UICommandInvoker invoker;

    public RepeatLocalCommand(UIReceiver ui, UICommandInvoker invoker) {
        super(ui);
        this.invoker = invoker;
    }

    @Override
    public String getExternalName() {
        return "repeat";
    }

    @Override
    public ClientCommandResponse<? extends UserPrintableAPICommandResponse> execute(String cmdName, Map<String, Object> args) {
        var status = invoker.invokeLast();
        return new ClientCommandResponse<>(status, "", null);
    }
}
