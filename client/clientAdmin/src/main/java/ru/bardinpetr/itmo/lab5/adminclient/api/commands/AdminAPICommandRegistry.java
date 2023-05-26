package ru.bardinpetr.itmo.lab5.adminclient.api.commands;

import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.models.commands.api.InfoCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.SaveCommand;

import java.util.List;

public class AdminAPICommandRegistry extends APICommandRegistry {

    public AdminAPICommandRegistry() {
        super(List.of(
                new InfoCommand(),
                new SaveCommand()
        ));
    }
}
