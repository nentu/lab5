package ru.bardinpetr.itmo.lab5.mainclient.local;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.impl.RAMCredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.api.connectors.net.UDPAPIClientFactory;
import ru.bardinpetr.itmo.lab5.client.ui.ClientConsoleArgumentsParser;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.mainclient.local.controller.auth.api.JWTAuthConnector;
import ru.bardinpetr.itmo.lab5.mainclient.local.controller.auth.api.StoredJWTCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.api.AddCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.ClearCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.GetWorkerIdsCommand;
import ru.bardinpetr.itmo.lab5.models.commands.api.InfoCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.AuthCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.PasswordLoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.JWTLoginResponse;
import ru.bardinpetr.itmo.lab5.models.data.Coordinates;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.JWTAPICommandAuthenticator;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

public class StressTest {

    private static final ArrayList<APIClientConnector> connectors = new ArrayList<>();

    public static void main(String[] args) {
        var argParse = new ClientConsoleArgumentsParser(args);

        for (int i = 0; i < 2; i++)
            connectors.add(createAPI(argParse.getServerFullAddr()));

        try {
            connectors.get(0).call(new ClearCommand());
        } catch (APIClientException e) {
            System.err.println(e.getMessage());
            return;
        }

        var executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100000; i++) {
            int finalI = i;
            executor.execute(() -> {
                try {
                    var api = getAPI(finalI);
                    var res = api.call(new AddCommand(new Worker(
                            0,
                            null,
                            ZonedDateTime.now(),
                            1,
                            "test%d".formatted(finalI),
                            234,
                            Date.from(Instant.now()),
                            null,
                            new Coordinates(123, 123),
                            null,
                            null))).getStatus();
                    var inf = api.call(new InfoCommand());
                    if (inf.isSuccess())
                        System.out.println(((InfoCommand.InfoCommandResponse) inf).getResult().getItemsCount());
                } catch (APIClientException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        try {
            System.out.println("END");
            var res = connectors.get(0).call(new GetWorkerIdsCommand());
            System.out.println(((GetWorkerIdsCommand.GetWorkerIdsCommandResponse) res).getResult().size());
        } catch (APIClientException e) {
        }
    }

    private static APIClientConnector getAPI(int id) {

        return connectors.get(id % connectors.size());
    }

    private static APIClientConnector createAPI(InetSocketAddress serverFullAddr) {
        var apiCredStorage = new RAMCredentialsStorage<StoredJWTCredentials>();
        var baseAPI = new UDPAPIClientFactory(serverFullAddr).create();
        var authedAPI = new JWTAuthConnector(
                JWTAPICommandAuthenticator.getInstance(),
                apiCredStorage,
                baseAPI
        );

        var cmd = new PasswordLoginCommand();
        cmd.setCredentials(new DefaultAuthenticationCredentials("u", "p"));
        AuthCommand.AuthCommandResponse login;
        try {
            login = (AuthCommand.AuthCommandResponse) authedAPI.call(cmd);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return null;
        }

        apiCredStorage.setCredentials(new StoredJWTCredentials((JWTLoginResponse) login.getData()));
        System.out.println("Initialized client");
        return authedAPI;
    }
}
