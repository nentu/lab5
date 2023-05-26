package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.*;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

/**
 * Class of organization add command
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AddOrgCommand extends UserAPICommand {
    @NonNull
    public Organization element;

    @Override
    public String getType() {
        return "add_org";
    }

    @Override
    public Field<?>[] getInteractArgs() {
        return new Field[]{
                new Field<>("element", Organization.class)
        };
    }

//    @Override
//    public ValidationResponse validate() {
//        return (new WorkerValidator()).validateAll(element);
//    }

    @Override
    public AddOrgCommandResponse createResponse() {
        return new AddOrgCommandResponse();
    }

    @Getter
    @Setter
    public static class AddOrgCommandResponse extends APICommandResponse implements UserPrintableAPICommandResponse {
        private Integer id;

        @Override
        public String getUserMessage() {
            return "Success with ID%d".formatted(id);
        }
    }
}
