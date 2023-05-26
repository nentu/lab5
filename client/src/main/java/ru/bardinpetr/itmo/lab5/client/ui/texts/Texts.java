package ru.bardinpetr.itmo.lab5.client.ui.texts;


import java.util.HashMap;
import java.util.Map;

public class Texts {

    private final static Map<TextKeys, String> textList = new HashMap<>() {{
        put(TextKeys.WORKERINTERACT, "Enter employee details");
        put(TextKeys.APIEXCEPTION, "Server is not responding. Try later or contact support");
    }};

    public static String get(TextKeys key) {
        return textList.get(key);
    }

    /**
     * Enum class for text access
     */
    public enum TextKeys {
        WORKERINTERACT, APIEXCEPTION,
    }
}
