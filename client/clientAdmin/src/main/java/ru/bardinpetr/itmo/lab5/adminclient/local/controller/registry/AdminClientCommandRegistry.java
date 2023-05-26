package ru.bardinpetr.itmo.lab5.adminclient.local.controller.registry;

import ru.bardinpetr.itmo.lab5.adminclient.local.controller.commands.HelpLocalCommand;
import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.commands.ExitLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.GeneralAPIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.registry.CommandRegistry;
import ru.bardinpetr.itmo.lab5.client.ui.cli.ScriptExecutor;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;

public class AdminClientCommandRegistry extends CommandRegistry {
    private final APICommandRegistry registry;

    public AdminClientCommandRegistry(APIClientConnector api, ScriptExecutor scriptExecutor, UIReceiver ui, APICommandRegistry registry) {
        super(api, scriptExecutor);

        this.registry = registry;

        register(new ExitLocalCommand(ui));
        register(new HelpLocalCommand(ui));
        registerFromAPI(
                registry.getCommands(),
                new GeneralAPIUILocalCommand(api, ui, registry)
        );
    }

    @Override
    public AdminClientCommandRegistry withUI(UIReceiver otherUI) {
        return new AdminClientCommandRegistry(api, scriptExecutor, otherUI, registry);
    }
}
