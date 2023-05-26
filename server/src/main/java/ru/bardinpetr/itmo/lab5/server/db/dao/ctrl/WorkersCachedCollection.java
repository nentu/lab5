package ru.bardinpetr.itmo.lab5.server.db.dao.ctrl;

import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.models.data.collection.CollectionInfo;
import ru.bardinpetr.itmo.lab5.models.data.collection.WorkerCollection;
import ru.bardinpetr.itmo.lab5.server.db.dto.OrganizationDTO;
import ru.bardinpetr.itmo.lab5.server.db.dto.WorkerDTO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class WorkersCachedCollection implements IWorkerCollectionDAO {

    private final PGWorkerStorageBackend backend;
    private final WorkerCollection collection;

    public WorkersCachedCollection(PGWorkerStorageBackend backend) {
        this.backend = backend;
        collection = this.backend.loadCollection();
    }

    @Override
    public CollectionInfo getCollectionInfo() {
        return new CollectionInfo(
                WorkerCollection.class.getSimpleName(),
                Worker.class.getName(),
                backend.getInfo().getInitializationDate(),
                collection.size()
        );
    }

    @Override
    public Stream<Worker> asStream() {
        return collection.stream();
    }

    @Override
    public Worker read(Integer id) {
        return asStream().filter(data -> data.getPrimaryKey().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Worker> readAll() {
        return asStream().toList();
    }


    @Override
    public boolean has(Integer id) {
        return asStream().anyMatch(i -> i.getPrimaryKey().equals(id));
    }

    @Override
    public Worker getMax() {
        try {
            return collection.last();
        } catch (NoSuchElementException ignored) {
            return null;
        }
    }

    @Override
    public Worker getMin() {
        try {
            return collection.first();
        } catch (NoSuchElementException ignored) {
            return null;
        }
    }

    @Override
    public Integer nextPrimaryKey() {
        throw new RuntimeException("Should not be used");
    }

    @Override
    public List<Organization> getOrganizations() {
        return backend
                .getTableProvider()
                .getOrganizations()
                .select()
                .stream()
                .map(i -> new Organization(i.id(), i.fullName(), i.type()))
                .toList();
    }

    @Override
    public void clear() {
        collection.clear();
        backend.clearCollection();
    }

    @Override
    public Integer addOrg(Organization element) {
        return backend.getTableProvider().getOrganizations().insert(
                new OrganizationDTO(element.getId(), element.getFullName(), element.getType())
        );
    }

    @Override
    public Integer add(Worker worker) {
        var workers = backend.getTableProvider().getWorkers();
        var res = workers.insert(new WorkerDTO(worker));
        if (res == null)
            return null;

        var newWorker = worker.withId(res);
        OrganizationDTO org;
        if (worker.getOrganization() != null) {
            org = backend.getTableProvider().getOrganizations().select(worker.getOrganization().getId());

            newWorker.setOrganization(
                    new Organization(org.id(), org.fullName(), org.type())
            );
        } else newWorker.setOrganization(null);
        newWorker.setOwnerUsername(workers.select(res).ownerUsername());
        collection.add(newWorker);

        return res;
    }

    @Override
    public boolean update(Integer id, Worker updateWorker) {
        var workers = backend.getTableProvider().getWorkers();
        var orig = workers.select(id);

        if (!workers.update(id, new WorkerDTO(updateWorker)))
            return false;

        var newWorker = updateWorker
                .withId(orig.id())
                .withOwner(orig.ownerId())
                .withCreationDate(orig.creationDate());

        var oldOrg = updateWorker.getOrganization();
        if (oldOrg != null) {
            var org = backend
                    .getTableProvider()
                    .getOrganizations()
                    .select(oldOrg.getId());
            newWorker.setOrganization(
                    new Organization(org.id(), org.fullName(), org.type())
            );
        }

        collection.removeIf(i -> i.getId().equals(id));
        newWorker.setOwnerUsername(workers.select(id).ownerUsername());

        collection.add(newWorker);

        return true;
    }

    @Override
    public boolean remove(Integer id) {
        var workers = backend.getTableProvider().getWorkers();
        if (!workers.delete(id))
            return false;

        collection.removeIf(i -> i.getId().equals(id));
        return true;
    }
}

