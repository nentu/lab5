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
public class GetOrgsCommand extends UserAPICommand {
    @Override
    public String getType() {
        return "get_orgs";
    }

    @Override
    public OrganisationCommandResponse createResponse() {
        return new OrganisationCommandResponse();
    }

    @Getter
    @Setter
    public static class OrganisationCommandResponse extends APICommandResponse implements UserPrintableAPICommandResponse {
        private List<Organization> organizations;

        @Override
        public String getUserMessage() {
            var s = new StringBuilder("organizations:\n");
            for (Organization i : organizations)
                s.append(
                        "\tID%s, full name: %s, type: %s\n".formatted(
                                i.getId(),
                                i.getFullName(),
                                i.getType()
                        )
                );
            return s.toString();
        }
    }
}
