package ru.bardinpetr.itmo.lab5.models.commands.responses;

import java.util.List;

public class ListAPICommandResponse<T> extends APICommandResponse {
    private List<T> result;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    @Override
    public String getUserMessage() {
        return getResult().toString();
    }
}
