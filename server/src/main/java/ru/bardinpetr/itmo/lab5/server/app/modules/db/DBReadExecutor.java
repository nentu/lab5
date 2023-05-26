package ru.bardinpetr.itmo.lab5.server.app.modules.db;

import ru.bardinpetr.itmo.lab5.common.executor.Executor;
import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.models.commands.api.*;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

import java.util.ArrayList;

public class DBReadExecutor extends Executor {
    private final ICollectionDAO<Integer, Worker> dao;

    public DBReadExecutor(ICollectionDAO<Integer, Worker> dao) {
        this.dao = dao;

        registerOperation(
                InfoCommand.class,
                req -> {
                    var resp = req.createResponse();
                    resp.setResult(dao.getCollectionInfo());
                    return resp;
                });

        registerOperation(
                GetWorkerCommand.class,
                req -> {
                    var resp = req.createResponse();
                    var worker = dao.read(req.getId());
                    if (worker == null) {
                        resp.setTextualResponse("No worker with id %d".formatted(req.getId()));
                        resp.setStatus(APIResponseStatus.CLIENT_ERROR);
                    } else {
                        resp.setWorker(worker);
                    }
                    return resp;
                });

        registerOperation(
                GetWorkerIdsCommand.class,
                req -> {
                    var resp = req.createResponse();
                    resp.setResult(dao.getAllMapped(Worker::getId));
                    return resp;
                });

        registerOperation(
                GetOrgsCommand.class,
                req -> {
                    var resp = req.createResponse();
                    resp.setOrganizations(dao.getOrganizations());
                    return resp;
                });

        registerOperation(UniqueOrganisationCommand.class, this::uniqueOrgs);
        registerOperation(FilterLessPosCommand.class, this::filterLess);
    }

    private UniqueOrganisationCommand.UniqueOrganisationCommandResponse uniqueOrgs(UniqueOrganisationCommand req) {
        var resp = req.createResponse();
        var data = dao.getOrganizations();
        for (var i : data) i.setId(0);
        resp.setOrganizations(data.stream().distinct().sorted(Organization.getComparator()).toList());
        return resp;
    }

    private FilterLessPosCommand.FilterLessPosCommandResponse filterLess(FilterLessPosCommand req) {
        var data = new ArrayList<>(dao.filter(worker -> (worker.getPosition() != null && worker.getPosition().compareTo(req.position) < 0)));
        var resp = req.createResponse();
        data.sort(Worker.getComparator());
        resp.setResult(data);
        return resp;
    }
}
