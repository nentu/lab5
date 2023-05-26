package ru.bardinpetr.itmo.lab5.server.db.factories;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.db.auth.BasicAuthProvider;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.PGDBConnector;
import ru.bardinpetr.itmo.lab5.server.db.dao.DBTableProvider;

@Slf4j
public class TableProviderFactory {
    public static DBTableProvider create(String url, String username, String password, boolean bootstrap) {
        try {
            var tableProvider = new DBTableProvider(
                    new PGDBConnector(
                            url,
                            new BasicAuthProvider(username, password)
                    )
            );

            if (bootstrap)
                tableProvider.bootstrap();

            return tableProvider;
        } catch (Throwable e) {
            log.error("Failed to start DB: {}", e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
