package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.api.error.APIUIException;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.api.GetSelfInfoCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.GetWorkerCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.GetWorkerIdsCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.UpdateCommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

import java.util.List;
import java.util.Map;


/**
 * Worker update command
 */
public class UpdateLocalCommand extends APIUILocalCommand {

    public UpdateLocalCommand(APIClientConnector api, UIReceiver ui, APICommandRegistry registry) {
        super(api, ui, registry);
    }

    @Override
    public String getExternalName() {
        return "update";
    }

    @Override
    protected UserAPICommand retrieveAPICommand(String name) {
        return new UpdateCommand();
    }

    /**
     * Builds update command with checks of ID and with use of default values
     *
     * @param name command name
     * @param args arguments
     * @return UpdateCommand
     */
    @Override
    protected APICommand prepareAPIMessage(String name, Map<String, Object> args) {
        var id = (Integer) args.get("id");
        if (id == null)
            throw new RuntimeException("Object id to update not specified");

        APICommandResponse availableIdsResp;
        try {
            availableIdsResp = apiClientReceiver.call(new GetWorkerIdsCommand());
        } catch (APIClientException e) {
            throw new APIUIException(e);
        }
        if (!availableIdsResp.isSuccess())
            throw new RuntimeException("Could not retrieve existing data: " + availableIdsResp.getUserMessage());


        var ids = ((GetWorkerIdsCommand.GetWorkerIdsCommandResponse) availableIdsResp).getResult();
        if (!ids.contains(id))
            throw new RuntimeException("No worker with such id");

        APICommandResponse currentObjResp;
        try {
            currentObjResp = apiClientReceiver.call(new GetWorkerCommand(id));
        } catch (APIClientException e) {
            throw new APIUIException(e);
        }
        if (!currentObjResp.isSuccess())
            throw new RuntimeException("Could not retrieve existing data: " + currentObjResp.getUserMessage());
        var current = ((GetWorkerCommand.GetWorkerCommandResponse) currentObjResp).getWorker();


        APICommandResponse accessCheckResp;
        try {
            accessCheckResp = apiClientReceiver.call(new GetSelfInfoCommand());
        } catch (APIClientException e) {
            throw new APIUIException(e);
        }
        if (!accessCheckResp.isSuccess())
            throw new RuntimeException("Could not retrieve existing data: " + currentObjResp.getUserMessage());

        if (!((GetSelfInfoCommand.GetSelfInfoResponse) accessCheckResp).getId().equals(current.getOwner())) {
            throw new RuntimeException("Object not owned by you");
        }


        return new UpdateCommand(
                id,
                uiReceiver.fill(Worker.class, current, List.of("fullName", "type"))
        );
    }
}
