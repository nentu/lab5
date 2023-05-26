package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.models.commands.requests.PagingAPICommand;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

/**
 * Class of print_descending command
 */
@Data
@NoArgsConstructor
public class PrintDescendingCommand extends PagingAPICommand {

    public PrintDescendingCommand(@NonNull Integer offset, @NonNull Integer count) {
        super(offset, count);
    }

    @Override
    public String getType() {
        return "print_descending";
    }

    @Override
    public PrintDescendingCommandResponse createResponse() {
        return new PrintDescendingCommandResponse();
    }

    public static class PrintDescendingCommandResponse extends DefaultCollectionCommandResponse {
        @Override
        public String getUserMessage() {
            var result = getResult();
            return "collection elements in descending order" +
                    Worker.nicePrintFormat(result);
        }
    }
}
