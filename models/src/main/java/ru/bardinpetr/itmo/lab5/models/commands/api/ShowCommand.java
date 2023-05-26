package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.models.commands.requests.PagingAPICommand;

/**
 * Class of show command
 */
@Data
@NoArgsConstructor
public class ShowCommand extends PagingAPICommand {
    public ShowCommand(@NonNull Integer offset, @NonNull Integer count) {
        super(offset, count);
    }

    @Override
    public String getType() {
        return "show";
    }

    @Override
    public ShowCommandResponse createResponse() {
        return new ShowCommandResponse();
    }

    public static class ShowCommandResponse extends DefaultCollectionCommandResponse {
    }
}
