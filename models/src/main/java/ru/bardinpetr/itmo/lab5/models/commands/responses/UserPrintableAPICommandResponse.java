package ru.bardinpetr.itmo.lab5.models.commands.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("userMessage")
public interface UserPrintableAPICommandResponse {
    default String getUserMessage() {
        return toString();
    }
}
