package ru.bardinpetr.itmo.lab5.adminclient.local.ui.texts;


import ru.bardinpetr.itmo.lab5.client.ui.texts.Texts;

import java.util.HashMap;
import java.util.Map;

public class AdminTexts extends Texts {

    private final static Map<TextKeys, String> textList = new HashMap<>() {{
        put(TextKeys.GREEETING, "Hi! It's Bardin Petr and Zaytsev Artem lab5 admin client. Enter \"help\" to see more information");
        put(TextKeys.HELP, """
                Below is a list of commands in the following form
                <command name> <arguments>: command description
                Arguments are entered on the line with the room. If the argument is specified in {}, then its input occurs in stages.
                		
                - help : display help on available commands
                - info : print information about the collection to standard output (type, initialization date, number of elements, etc.)
                - save : save db to file on server
                - exit : exit the program (without saving to a file)
                  """
        );
    }};


    public static String get(TextKeys key) {
        return textList.get(key);
    }

    public enum TextKeys {
        GREEETING,
        HELP
    }
}
