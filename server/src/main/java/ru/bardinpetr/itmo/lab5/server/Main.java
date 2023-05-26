package ru.bardinpetr.itmo.lab5.server;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.log.SetupJUL;
import ru.bardinpetr.itmo.lab5.db.auth.BasicAuthProvider;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.PGDBConnector;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.ErrorHandlingApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.UDPInputTransportApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.UDPOutputTransportApplication;
import ru.bardinpetr.itmo.lab5.network.transport.server.UDPServerFactory;
import ru.bardinpetr.itmo.lab5.server.app.modules.auth.AuthAppFacade;
import ru.bardinpetr.itmo.lab5.server.app.modules.db.DBApplicationFacade;
import ru.bardinpetr.itmo.lab5.server.app.ui.ServerConsoleArgumentsParser;
import ru.bardinpetr.itmo.lab5.server.db.dao.DBTableProvider;

@Slf4j
public class Main {
    public static void main(String[] args) {
        SetupJUL.loadProperties(Main.class);
        var consoleArgs = new ServerConsoleArgumentsParser(args);

        var tableProvider = new DBTableProvider(
                new PGDBConnector(
                        consoleArgs.getDatabaseUrl(),
                        new BasicAuthProvider(consoleArgs.getUsername(), consoleArgs.getPassword())
                )
        );
        if (consoleArgs.doBootstrap()) tableProvider.bootstrap();

        var udpServer = UDPServerFactory.create(consoleArgs.getPort());
        var mainApp = new UDPInputTransportApplication(udpServer);
        mainApp
                .chain(new UDPOutputTransportApplication(udpServer))
                .chain(AuthAppFacade.create(tableProvider))
                .chain(DBApplicationFacade.create(tableProvider))
                .chain(new ErrorHandlingApplication());

        mainApp.start();
    }
}
