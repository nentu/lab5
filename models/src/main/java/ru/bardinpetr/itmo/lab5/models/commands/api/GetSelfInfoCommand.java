package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;

@Data
public class GetSelfInfoCommand extends UserAPICommand {
    @Override
    public String getType() {
        return "self";
    }

    @Override
    public GetSelfInfoResponse createResponse() {
        return new GetSelfInfoResponse();
    }

    public static class GetSelfInfoResponse extends APICommandResponse implements UserPrintableAPICommandResponse {
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String getUserMessage() {
            return String.valueOf(id);
        }
    }
}
