package ru.bardinpetr.itmo.lab5.server.db.dao.ctrl;

import ru.bardinpetr.itmo.lab5.db.backend.DBStorageBackend;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.models.data.collection.CollectionInfo;
import ru.bardinpetr.itmo.lab5.models.data.collection.WorkerCollection;
import ru.bardinpetr.itmo.lab5.server.db.dao.DBTableProvider;
import ru.bardinpetr.itmo.lab5.server.db.dto.WorkerDTO;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

public class PGWorkerStorageBackend implements DBStorageBackend<WorkerCollection> {

    private final DBTableProvider tableProvider;

    public PGWorkerStorageBackend(DBTableProvider tableProvider) {
        this.tableProvider = tableProvider;
    }

    @Override
    public WorkerCollection loadCollection() {
        WorkerCollection collection = new WorkerCollection();
        var t = tableProvider.getWorkers().select();
        for (var worker : t) {
            var org = tableProvider.getOrganizations().select(worker.organizationId());
            Organization resOrg = null;
            if (org != null) {
                resOrg = new Organization(
                        org.id(),
                        org.fullName(),
                        org.type()
                );
            }
            collection.add(new Worker(
                    worker.id(),
                    worker.ownerUsername(),
                    worker.creationDate(),
                    worker.ownerId(),
                    worker.name(),
                    worker.salary(),
                    worker.startDate(),
                    worker.endDate(),
                    worker.coordinates(),
                    resOrg,
                    worker.position()
            ));
        }

        return collection;
    }

    @Override
    public void clearCollection() {
        tableProvider.getOrganizations().truncate();
        tableProvider.getWorkers().truncate();
    }

    @Override
    public CollectionInfo getInfo() {
        var x = getTableProvider().getWorkers().select().stream().map(WorkerDTO::creationDate).min(ChronoZonedDateTime::compareTo);
        return new CollectionInfo(
                null,
                null,
                x.orElseGet(ZonedDateTime::now),
                null
        );
    }

    public DBTableProvider getTableProvider() {
        return tableProvider;
    }
}
