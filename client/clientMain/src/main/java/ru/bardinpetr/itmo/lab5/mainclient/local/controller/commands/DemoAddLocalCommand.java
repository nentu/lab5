package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.api.AddCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.AddOrgCommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.data.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Random;


/**
 * Worker insert command for demonstration purposes with random data
 */
public class DemoAddLocalCommand extends APIUILocalCommand {

    private final Random rng;

    public DemoAddLocalCommand(APIClientConnector api, UIReceiver ui, APICommandRegistry registry) {
        super(api, ui, registry);
        this.rng = new Random();
    }

    @Override
    public String getExternalName() {
        return "addd";
    }

    @Override
    protected UserAPICommand retrieveAPICommand(String name) {
        return new AddCommand();
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
        AddOrgCommand.AddOrgCommandResponse res;
        try {
            res = (AddOrgCommand.AddOrgCommandResponse) apiClientReceiver.call(new AddOrgCommand(new Organization(-1, randomString(5), OrganizationType.PUBLIC)));
        } catch (APIClientException e) {
            throw new RuntimeException(e);
        }

        return new AddCommand(
                new Worker(
                        -1,
                        "Artem",
                        ZonedDateTime.now(),
                        null,
                        randomString(20),
                        rng.nextFloat() * 1000,
                        Date.from(Instant.now()),
                        LocalDate.now(),
                        new Coordinates(
                                rng.nextInt(0, 100),
                                rng.nextFloat(0, 100)
                        ),
                        new Organization(
                                res.getId(),
                                null,
                                null
                        ),
                        Position.values()[rng.nextInt(0, Position.values().length - 1)]
                )
        );
    }

    private String randomString(int len) {
        return "%x".formatted(rng.nextInt(100000, Integer.MAX_VALUE));
    }
}
