package ru.bardinpetr.itmo.lab5.client.controller.commands;

import ru.bardinpetr.itmo.lab5.client.controller.common.UILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;

import java.util.Map;


public class ExitLocalCommand extends UILocalCommand {

    public ExitLocalCommand(UIReceiver ui) {
        super(ui);
    }

    @Override
    public String getExternalName() {
        return "exit";
    }

    @Override
    public ClientCommandResponse<? extends UserPrintableAPICommandResponse> execute(String cmdName, Map<String, Object> args) {
        System.exit(0);
        return ClientCommandResponse.ok();
    }
}
