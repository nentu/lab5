package ru.bardinpetr.itmo.lab5.mainclient.local.controller.registry;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.auth.ICredentialsStorage;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.commands.ExitLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.commands.RepeatLocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.GeneralAPIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.registry.CommandRegistry;
import ru.bardinpetr.itmo.lab5.client.ui.cli.ScriptExecutor;
import ru.bardinpetr.itmo.lab5.client.ui.cli.UICommandInvoker;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands.*;

public class MainClientCommandRegistry extends CommandRegistry {
    private final APICommandRegistry registry;
    private final UICommandInvoker invoker;
    private final ICredentialsStorage credentialsStorage;

    public MainClientCommandRegistry(APIClientConnector api, ScriptExecutor scriptExecutor, UIReceiver ui, APICommandRegistry registry, UICommandInvoker invoker, ICredentialsStorage credentialsStorage) {
        super(api, scriptExecutor);

        this.invoker = invoker;
        this.registry = registry;
        this.credentialsStorage = credentialsStorage;

        register(new ExitLocalCommand(ui));
        register(new HelpLocalCommand(ui));
        register(new RepeatLocalCommand(ui, invoker));
        register(new AddLocalCommand(api, ui, registry));
        register(new AddOrgLocalCommand(api, ui, registry));
        register(new DemoAddLocalCommand(api, ui, registry));
        register(new UpdateLocalCommand(api, ui, registry));
        register(new ShowLocalCommand(api, ui, registry));
        register(new DescShowLocalCommand(api, ui, registry));
        register(new ScriptLocalCommand(api, ui, scriptExecutor, registry));
        registerFromAPI(
                registry.getCommands(),
                new GeneralAPIUILocalCommand(api, ui, registry)
        );
    }

    @Override
    public MainClientCommandRegistry withUI(UIReceiver otherUI) {
        return new MainClientCommandRegistry(api, scriptExecutor, otherUI, registry, invoker, credentialsStorage);
    }
}
