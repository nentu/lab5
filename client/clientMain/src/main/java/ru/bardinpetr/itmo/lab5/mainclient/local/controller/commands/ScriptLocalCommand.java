package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.ui.cli.ScriptExecutor;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.NotRepeatableException;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.ScriptException;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.common.io.exceptions.FileAccessException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

import java.util.List;
import java.util.Map;


/**
 * Command for processing nested scripts
 */
public class ScriptLocalCommand extends APIUILocalCommand {
    private final ScriptExecutor scriptExecutor;

    public ScriptLocalCommand(APIClientConnector api, UIReceiver ui, ScriptExecutor scriptExecutor, APICommandRegistry registry) {
        super(api, ui, registry);
        this.scriptExecutor = scriptExecutor;
    }

    @Override
    public List<Field<?>> getCommandInlineArgs(String cmdName) {
        return List.of(new Field[]{new Field<>("fileName", String.class)});
    }

    @Override
    protected APICommand prepareAPIMessage(String name, Map<String, Object> args) {
        return null;
    }

    @Override
    public String getExternalName() {
        return "execute_script";
    }

    /**
     * Using ScriptExecutor recursively parse script file
     *
     * @param cmdName should be "execute_script"
     * @param args    should contain "fileName" key with script file path
     * @return execution result
     */
    @Override
    public ClientCommandResponse execute(String cmdName, Map<String, Object> args) {
        String path = (String) args.get("fileName");
        if (path == null)
            throw new RuntimeException("No script file passed");
        try {
            scriptExecutor.process(path);
        } catch (FileAccessException e) {
            throw new RuntimeException("Can't get access to script");
        } catch (NotRepeatableException | ScriptException e) {
            throw new RuntimeException("Invalid Script");
        }

        return ClientCommandResponse.ok();
    }
}
