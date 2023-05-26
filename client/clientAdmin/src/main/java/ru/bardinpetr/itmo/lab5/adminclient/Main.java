package ru.bardinpetr.itmo.lab5.adminclient;

import ru.bardinpetr.itmo.lab5.adminclient.api.commands.AdminAPICommandRegistry;
import ru.bardinpetr.itmo.lab5.adminclient.api.commands.AdminAPICommandsDescriptionHolder;
import ru.bardinpetr.itmo.lab5.adminclient.local.controller.registry.AdminClientCommandRegistry;
import ru.bardinpetr.itmo.lab5.adminclient.local.ui.texts.AdminTexts;
import ru.bardinpetr.itmo.lab5.client.api.connectors.net.UDPAPIClientFactory;
import ru.bardinpetr.itmo.lab5.client.ui.ClientConsoleArgumentsParser;
import ru.bardinpetr.itmo.lab5.client.ui.cli.CLIController;
import ru.bardinpetr.itmo.lab5.client.ui.cli.Interpreter;
import ru.bardinpetr.itmo.lab5.client.ui.cli.ScriptExecutor;
import ru.bardinpetr.itmo.lab5.client.ui.cli.UICommandInvoker;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.ConsolePrinter;

public class Main {
    public static void main(String[] args) {
        var argParse = new ClientConsoleArgumentsParser(args);

        var apiConnector =
                new UDPAPIClientFactory(argParse.getServerFullAddr())
                        .create();

        var descriptionHolder = new AdminAPICommandsDescriptionHolder();

        var uiController = new CLIController(
                descriptionHolder,
                new ConsolePrinter(),
                System.in,
                true);
        uiController.display(AdminTexts.get(AdminTexts.TextKeys.GREEETING));

        var invoker = new UICommandInvoker(uiController);
        var scriptExecutor = new ScriptExecutor(
                descriptionHolder,
                invoker
        );

        var apiRegistry = new AdminAPICommandRegistry();
        var registry = new AdminClientCommandRegistry(apiConnector, scriptExecutor, uiController, apiRegistry);

        var interpreter = new Interpreter(registry, uiController, invoker);
        interpreter.run();
    }
}
