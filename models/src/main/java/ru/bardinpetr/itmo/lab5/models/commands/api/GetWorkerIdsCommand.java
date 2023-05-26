package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.ListAPICommandResponse;

/**
 * Class of show command
 */
@Data
public class GetWorkerIdsCommand extends UserAPICommand {
    @Override
    public String getType() {
        return "gwi";
    }

    @Override
    public GetWorkerIdsCommandResponse createResponse() {
        return new GetWorkerIdsCommandResponse();
    }

    public static class GetWorkerIdsCommandResponse extends ListAPICommandResponse<Integer> {
    }
}
