package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.ListAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ExecuteScriptCommand extends UserAPICommand {
    @NonNull
    private List<APICommand> commands;

    @Override
    public String getType() {
        return "execute_script";
    }

    @Override
    public Field<?>[] getInlineArgs() {
        return new Field[]{
                new Field<>("fileName", String.class)
        };
    }

    @Override
    public ExecuteScriptCommandResponse createResponse() {
        return new ExecuteScriptCommandResponse();
    }

    public static class ExecuteScriptCommandResponse extends ListAPICommandResponse<APICommandResponse> {
        @Override
        public String getUserMessage() {
            var respond = new StringBuilder();
            for (var i : getResult())
                respond
                        .append(
                                i instanceof UserPrintableAPICommandResponse ?
                                        i.getUserMessage() : i.toString()
                        )
                        .append("\n");
            return respond.toString();
        }
    }
}