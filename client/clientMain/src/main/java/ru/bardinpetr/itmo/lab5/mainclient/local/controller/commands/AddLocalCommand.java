package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.api.AddCommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

import java.util.List;


/**
 * Worker insert command for demonstration purposes with random data
 */
public class AddLocalCommand extends APIUILocalCommand {

    public AddLocalCommand(APIClientConnector api, UIReceiver ui, APICommandRegistry registry) {
        super(api, ui, registry);
    }

    @Override
    public String getExternalName() {
        return "add";
    }

    @Override
    protected UserAPICommand retrieveAPICommand(String name) {
        return new AddCommand();
    }

    @Override
    protected <T> T handleInteractArg(Field<T> field) {
        return uiReceiver.fill(field.getValueClass(), null, List.of("fullName", "type"));
    }
}
