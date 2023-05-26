package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;

/**
 * Class of save command
 */
@Data
public class SaveCommand extends UserAPICommand {
    @Override
    public String getType() {
        return "save";
    }
}
