package ru.bardinpetr.itmo.lab5.server.app.modules.db;

import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.IOwnedCollectionDAO;
import ru.bardinpetr.itmo.lab5.models.commands.api.*;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.network.app.server.AbstractApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;

import static ru.bardinpetr.itmo.lab5.server.app.utils.AppUtils.extractUser;

public class DBInsertApplication extends AbstractApplication {
    private final IOwnedCollectionDAO<Integer, Worker> dao;

    public DBInsertApplication(IOwnedCollectionDAO<Integer, Worker> dao) {
        this.dao = dao;

        on(AddCommand.class, this::add);
        on(AddIfMaxCommand.class, this::addIfMax);
        on(AddIfMinCommand.class, this::addIfMin);
        on(AddOrgCommand.class, this::addOrg);
        on(GetSelfInfoCommand.class, this::selfInfo);
    }

    private void addOrg(AppRequest appRequest) {
        AddOrgCommand req = (AddOrgCommand) appRequest.payload();
        var resp = req.createResponse();
        var pk = dao.addOrg(req.element);
        if (pk == null) {
            appRequest.response().sendErr("Couldn't add");
            return;
        }
        resp.setId(pk);
        appRequest.response().from(resp).send();
    }

    private void add(AppRequest appRequest) {
        AddCommand req = (AddCommand) appRequest.payload();
        var resp = req.createResponse();
        var pk = dao.add(extractUser(appRequest), req.element);
        if (pk == null) {
            appRequest.response().sendErr("Couldn't add");
            return;
        }
        resp.setId(pk);
        appRequest.response().from(resp).send();
    }

    private void addIfMax(AppRequest appRequest) {
        var resp = appRequest.response();

        AddIfMaxCommand req = (AddIfMaxCommand) appRequest.payload();
        var cur = req.element;
        var curMax = dao.getMax();

        if (curMax == null) {
            resp
                    .message("No elements")
                    .status(APIResponseStatus.CLIENT_ERROR)
                    .send();
            return;
        }
        if (curMax.compareTo(cur) >= 0) {
            resp
                    .message("Not maximum")
                    .status(APIResponseStatus.CLIENT_ERROR)
                    .send();
            return;
        }

        dao.add(extractUser(appRequest), cur);
        resp.sendOk();
    }

    private void addIfMin(AppRequest appRequest) {
        var resp = appRequest.response();

        AddIfMinCommand req = (AddIfMinCommand) appRequest.payload();
        var cur = req.element;
        var curMin = dao.getMin();

        if (curMin == null) {
            resp
                    .message("No elements")
                    .status(APIResponseStatus.CLIENT_ERROR)
                    .send();
            return;
        }
        if (curMin.compareTo(cur) >= 0) {
            resp
                    .message("Not minimum")
                    .status(APIResponseStatus.CLIENT_ERROR)
                    .send();
            return;
        }

        dao.add(extractUser(appRequest), cur);
        resp.sendOk();
    }

    private void selfInfo(AppRequest request) {
        var resp = (GetSelfInfoCommand.GetSelfInfoResponse) request.payload().createResponse();
        resp.setId(extractUser(request));
        request.response().from(resp).send();
    }
}
