//package ru.bardinpetr.itmo.lab5.client;
//
//import ru.bardinpetr.itmo.lab5.client.api.auth.AuthenticatedConnectorDecorator;
//import ru.bardinpetr.itmo.lab5.client.api.auth.impl.RAMCredentialsStorage;
//import ru.bardinpetr.itmo.lab5.client.api.connectors.net.UDPAPIClientFactory;
//import ru.bardinpetr.itmo.lab5.client.ui.ClientConsoleArgumentsParser;
//import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
//import ru.bardinpetr.itmo.lab5.models.commands.api.AddCommand;
//import ru.bardinpetr.itmo.lab5.models.commands.api.ClearCommand;
//import ru.bardinpetr.itmo.lab5.models.commands.api.GetWorkerIdsCommand;
//import ru.bardinpetr.itmo.lab5.models.commands.api.InfoCommand;
//import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
//import ru.bardinpetr.itmo.lab5.models.data.Coordinates;
//import ru.bardinpetr.itmo.lab5.models.data.Worker;
//import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.api.DefaultAPICommandAuthenticator;
//
//import java.time.Instant;
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.concurrent.Executors;
//
//public class StressTest {
//
//    public static void main(String[] args) {
//        var argParse = new ClientConsoleArgumentsParser(args);
//
//        var executor = Executors.newFixedThreadPool(16);
//        System.out.println(argParse.getServerFullAddr());
//        var apiConnector =
//                new UDPAPIClientFactory(argParse.getServerFullAddr())
//                        .create();
//
//        try {
//            apiConnector.call(new ClearCommand());
//        } catch (APIClientException e) {
//        }
//        for (int i = 0; i < 100000; i++) {
//            int finalI = i;
//            executor.execute(() -> {
//                try {
//                    var apiCredStorage = new RAMCredentialsStorage<DefaultAuthenticationCredentials>();
//                    var baseAPI = new UDPAPIClientFactory(argParse.getServerFullAddr()).create();
////                    var authedAPI = new JWTAuthConnector<>(
////                            DefaultAPICommandAuthenticator.getInstance(),
////                            apiCredStorage,
////                            baseAPI
////                    );
//                    apiCredStorage.setCredentials(new DefaultAuthenticationCredentials("u", "p"));
//
//                    authedAPI.call(new AddCommand(new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "u",
//                            "test%d".formatted(finalI),
//                            234,
//                            Date.from(Instant.now()),
//                            null,
//                            new Coordinates(123, 123),
//                            null,
//                            null))).getStatus();
////                    System.out.println(cur.call(new ClearCommand()));
//                    System.out.println(((InfoCommand.InfoCommandResponse) authedAPI.call(new InfoCommand())).getResult().getItemsCount());
////                    System.out.println(cur.call(new InfoCommand()).getTextualResponse());
//                } catch (APIClientException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//
//        executor.shutdown();
//        while (!executor.isTerminated()) {
//        }
//
//        try {
//            System.out.println("END");
//            var res = apiConnector.call(new GetWorkerIdsCommand());
//            System.out.println(((GetWorkerIdsCommand.GetWorkerIdsCommandResponse) res).getResult().size());
//        } catch (APIClientException e) {
//        }
//    }
//}
