package ru.bardinpetr.itmo.lab5.models.commands.requests;

import lombok.Data;
import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.models.commands.responses.ListAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.data.Worker;

/**
 * Class of show command
 */
@Data
public class PagingAPICommand extends UserAPICommand {
    public static final Integer FULL_COUNT = -1;

    @NonNull
    private Integer offset = 0;

    @NonNull
    private Integer count = FULL_COUNT;

    public PagingAPICommand() {
    }

    public PagingAPICommand(@NonNull Integer offset, @NonNull Integer count) {
        this.offset = offset;
        this.count = count;
    }

    @Override
    public String getType() {
        return "null";
    }

    @Override
    public DefaultCollectionCommandResponse createResponse() {
        return new DefaultCollectionCommandResponse();
    }

    public static class DefaultCollectionCommandResponse extends ListAPICommandResponse<Worker> {
        @Override
        public String getUserMessage() {
            var result = getResult();
            return "Collection" +
                    Worker.nicePrintFormat(result);
        }
    }
}
