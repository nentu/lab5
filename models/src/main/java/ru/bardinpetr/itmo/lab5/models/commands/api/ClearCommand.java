package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;

/**
 * Class of clear command
 */
@Data
public class ClearCommand extends UserAPICommand {
    @Override
    public String getType() {
        return "clear";
    }
}
