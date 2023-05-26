package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;
import ru.bardinpetr.itmo.lab5.models.data.Organization;

import java.util.List;

/**
 * Class of print_unique_organization  command
 */

@Data
public class UniqueOrganisationCommand extends UserAPICommand {
    @Override
    public String getType() {
        return "print_unique_organization";
    }

    @Override
    public UniqueOrganisationCommandResponse createResponse() {
        return new UniqueOrganisationCommandResponse();
    }

    @Getter
    @Setter
    public static class UniqueOrganisationCommandResponse extends APICommandResponse implements UserPrintableAPICommandResponse {
        private List<Organization> organizations;

        @Override
        public String getUserMessage() {
            var s = new StringBuilder("organization field unique values:\n");
            for (Organization i : organizations)
                s.append(
                        "\tFull name: %s, type: %s\n".formatted(
                                i.getFullName(),
                                i.getType()
                        )
                );
            return s.toString();
        }
    }
}
