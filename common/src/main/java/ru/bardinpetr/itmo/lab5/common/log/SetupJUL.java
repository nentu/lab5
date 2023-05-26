package ru.bardinpetr.itmo.lab5.common.log;

import java.io.IOException;
import java.util.logging.LogManager;

public class SetupJUL {
    public static void loadProperties(Class<?> cls) {
        try {
            LogManager
                    .getLogManager()
                    .readConfiguration(
                            cls.getResourceAsStream("/logger.properties")
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
