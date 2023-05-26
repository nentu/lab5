package ru.bardinpetr.itmo.lab5.mainclient.api.commands;

import ru.bardinpetr.itmo.lab5.client.api.description.APICommandsDescriptionHolder;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.DefaultAuthenticationCredentials;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

public class UserAPICommandsDescriptionHolder extends APICommandsDescriptionHolder {

    public UserAPICommandsDescriptionHolder() {
        super(new Class[]{Worker.class, DefaultAuthenticationCredentials.class});
    }
}
