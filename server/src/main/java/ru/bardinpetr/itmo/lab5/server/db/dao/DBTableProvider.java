package ru.bardinpetr.itmo.lab5.server.db.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.PGDBConnector;
import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;
import ru.bardinpetr.itmo.lab5.server.Main;
import ru.bardinpetr.itmo.lab5.server.db.dao.tables.OrganizationsPGDAO;
import ru.bardinpetr.itmo.lab5.server.db.dao.tables.UsersPGDAO;
import ru.bardinpetr.itmo.lab5.server.db.dao.tables.WorkersPGDAO;

@Slf4j
public class DBTableProvider {

    @Getter
    private final OrganizationsPGDAO organizations;
    @Getter
    private final WorkersPGDAO workers;
    @Getter
    private final UsersPGDAO users;

    private final PGDBConnector PGDBConnector;


    public DBTableProvider(PGDBConnector PGDBConnector) {
        this.PGDBConnector = PGDBConnector;

        try {
            organizations = new OrganizationsPGDAO(PGDBConnector);
            workers = new WorkersPGDAO(PGDBConnector);
            users = new UsersPGDAO(PGDBConnector);
        } catch (DBCreateException e) {
            throw new RuntimeException(e);
        }
    }

    public void bootstrap() {
        var res = PGDBConnector.bootstrap(Main.class.getResourceAsStream("/db/create.sql"));
        if (res) {
            log.info("DB bootstrapped successfully");
        } else {
            log.error("Failed to bootstrap DB");
            System.exit(1);
        }
    }
}
