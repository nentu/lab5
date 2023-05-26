package ru.bardinpetr.itmo.lab5.client.controller.common;

import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.UICallableCommand;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.common.serdes.ValueDeserializer;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for all commands that can be triggered from UI and have a receiver to communicate with the UI in real time
 */
public abstract class UILocalCommand extends AbstractLocalCommand implements UICallableCommand {
    public static final String NAME_ARG = "cmdName";
    protected final UIReceiver uiReceiver;
    private final ValueDeserializer valueDes;

    public UILocalCommand(UIReceiver receiver) {
        valueDes = new ValueDeserializer();
        uiReceiver = receiver;
    }

    /**
     * @return get name which should identify command on user input or null if not applicable
     */
    public abstract String getExternalName();

    /**
     * Inline arguments for calling command from TUI
     *
     * @param cmdName command name if command realization depends
     * @return list of fileds descriptions
     */
    protected List<Field<?>> getCommandInlineArgs(String cmdName) {
        return List.of();
    }

    /**
     * Like getCommandInlineArgs but have NAME_ARG as argument #0.
     */
    private List<Field<?>> getFullInlineArgs(String cmdName) {
        var data = new ArrayList<Field<?>>();
        data.add(new Field<>(NAME_ARG, String.class));
        var args = getCommandInlineArgs(cmdName);
        if (args != null) data.addAll(args);
        return data;
    }

    /**
     * Parses arguments according to field descriptions of command base object
     *
     * @param args list of arguments including command name as args[0]
     * @return command execution result
     */
    @Override
    public ClientCommandResponse<? extends UserPrintableAPICommandResponse> executeWithArgs(List<String> args) {
        if (args.size() == 0)
            throw new RuntimeException("Not command name");

        var defs = getFullInlineArgs(args.get(0));

        if (defs.size() != args.size())
            throw new RuntimeException("Not enough args");

        var objectMap = new HashMap<String, Object>();
        try {
            for (int i = 0; i < args.size(); i++)
                objectMap.put(
                        defs.get(i).getName(),
                        valueDes.deserialize(defs.get(i).getValueClass(), args.get(i))
                );
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Command arguments is of invalid type");
        }

        return execute((String) objectMap.get(NAME_ARG), objectMap);
    }

    @Override
    public abstract ClientCommandResponse<? extends UserPrintableAPICommandResponse> execute(String cmdName, Map<String, Object> args);
}
