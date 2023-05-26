package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.models.commands.api.AddOrgCommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

import java.util.List;


/**
 * Worker insert command for demonstration purposes with random data
 */
public class AddOrgLocalCommand extends APIUILocalCommand {

    public AddOrgLocalCommand(APIClientConnector api, UIReceiver ui, APICommandRegistry registry) {
        super(api, ui, registry);
    }

    @Override
    public String getExternalName() {
        return "add_org";
    }

    @Override
    protected UserAPICommand retrieveAPICommand(String name) {
        return new AddOrgCommand();
    }

    @Override
    protected <T> T handleInteractArg(Field<T> field) {
        return uiReceiver.fill(field.getValueClass(), null, List.of("id"));
    }
}
