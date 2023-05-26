package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

/**
 * Class of show command
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class GetWorkerCommand extends UserAPICommand {
    @NonNull
    public Integer id;

    @Override
    public String getType() {
        return "gw";
    }

    @Override
    public GetWorkerCommandResponse createResponse() {
        return new GetWorkerCommandResponse();
    }

    @Data
    @NoArgsConstructor
    public static class GetWorkerCommandResponse extends APICommandResponse implements UserPrintableAPICommandResponse {
        @NonNull
        private Worker worker;

        @Override
        public String getUserMessage() {
            return worker.toString();
        }
    }
}
