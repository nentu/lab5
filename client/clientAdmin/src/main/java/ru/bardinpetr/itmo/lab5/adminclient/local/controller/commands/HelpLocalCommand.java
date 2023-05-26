package ru.bardinpetr.itmo.lab5.adminclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.adminclient.local.ui.texts.AdminTexts;
import ru.bardinpetr.itmo.lab5.client.controller.common.UILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;

import java.util.Map;


/**
 * Command to print help
 */
public class HelpLocalCommand extends UILocalCommand {

    public HelpLocalCommand(UIReceiver ui) {
        super(ui);
    }

    @Override
    public String getExternalName() {
        return "help";
    }

    @Override
    public ClientCommandResponse execute(String cmdName, Map<String, Object> args) {
        return new ClientCommandResponse(true, AdminTexts.get(AdminTexts.TextKeys.HELP), null);
    }
}
