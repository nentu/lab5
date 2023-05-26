package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.requests.PagingAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;

/**
 * Class of show command
 */
@Data
@NoArgsConstructor
public class ShowMineCommand extends UserAPICommand {

    @Override
    public String getType() {
        return "mine";
    }

    @Override
    public ShowCommandResponse createResponse() {
        return new ShowCommandResponse();
    }

    public static class ShowCommandResponse extends PagingAPICommand.DefaultCollectionCommandResponse {
    }
}
