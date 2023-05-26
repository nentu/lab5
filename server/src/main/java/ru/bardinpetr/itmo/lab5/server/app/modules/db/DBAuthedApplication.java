package ru.bardinpetr.itmo.lab5.server.app.modules.db;

import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.IOwnedCollectionDAO;
import ru.bardinpetr.itmo.lab5.models.commands.api.ShowMineCommand;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.APIApplication;

public class DBAuthedApplication extends APIApplication {
    private final IOwnedCollectionDAO<Integer, Worker> dao;

    public DBAuthedApplication(IOwnedCollectionDAO<Integer, Worker> dao) {
        this.dao = dao;
        on(ShowMineCommand.class, this::onShowMine);
    }

    private void onShowMine(AppRequest appRequest) {
        ShowMineCommand req = (ShowMineCommand) appRequest.payload();
        var resp = req.createResponse();
        resp.setResult(dao.filter(i -> i.getOwner().equals(appRequest.session().getAuth().getUserHandle())));
        appRequest.response().from(resp).send();
    }
}
