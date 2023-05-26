package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;

/**
 * Class of add_if_max command
 */
@Data
public class AddIfMaxCommand extends AddCommand {
    @Override
    public String getType() {
        return "add_if_max";
    }
}
