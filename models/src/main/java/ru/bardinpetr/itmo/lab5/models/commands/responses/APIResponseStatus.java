package ru.bardinpetr.itmo.lab5.models.commands.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum APIResponseStatus {
    OK,
    CLIENT_ERROR,
    AUTH_ERROR,
    SERVER_ERROR,
    UNPROCESSED,
    NOT_FOUND;

    @JsonIgnore
    public boolean isError() {
        return this.equals(CLIENT_ERROR) || this.equals(SERVER_ERROR);
    }
}