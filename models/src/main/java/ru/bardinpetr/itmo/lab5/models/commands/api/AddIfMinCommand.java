package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;

/**
 * Class of add_if_min command
 */
@Data
public class AddIfMinCommand extends AddCommand {
    @Override
    public String getType() {
        return "add_if_min";
    }
}
