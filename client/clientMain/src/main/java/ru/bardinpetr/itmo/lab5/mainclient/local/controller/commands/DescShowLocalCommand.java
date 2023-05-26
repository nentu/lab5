package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.api.PrintDescendingCommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.PagingAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;

public class DescShowLocalCommand extends PagingLocalCommand {
    public DescShowLocalCommand(APIClientConnector api, UIReceiver ui, APICommandRegistry registry) {
        super(api, ui, registry);
    }

    @Override
    public String getExternalName() {
        return "print_descending";
    }

    @Override
    protected UserAPICommand retrieveAPICommand(String name) {
        return null;
    }

    @Override
    protected PagingAPICommand createPagedCommand(int offset, int count) {
        return new PrintDescendingCommand(offset, count);
    }
}
