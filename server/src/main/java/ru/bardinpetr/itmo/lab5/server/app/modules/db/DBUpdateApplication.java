package ru.bardinpetr.itmo.lab5.server.app.modules.db;

import ru.bardinpetr.itmo.lab5.db.frontend.adapters.owned.IOwnedCollectionDAO;
import ru.bardinpetr.itmo.lab5.models.commands.api.UpdateCommand;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.network.app.server.models.requests.AppRequest;
import ru.bardinpetr.itmo.lab5.network.app.server.special.impl.APIApplication;

import static ru.bardinpetr.itmo.lab5.server.app.utils.AppUtils.extractUser;

public class DBUpdateApplication extends APIApplication {
    private final IOwnedCollectionDAO<Integer, Worker> dao;

    public DBUpdateApplication(IOwnedCollectionDAO<Integer, Worker> dao) {
        this.dao = dao;
        on(UpdateCommand.class, this::onUpdate);
    }

    private void onUpdate(AppRequest appRequest) {
        UpdateCommand req = (UpdateCommand) appRequest.payload();
        dao.update(extractUser(appRequest), req.id, req.element);
        appRequest.response().sendOk();
    }
}
