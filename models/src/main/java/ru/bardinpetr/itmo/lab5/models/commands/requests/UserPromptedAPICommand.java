package ru.bardinpetr.itmo.lab5.models.commands.requests;

import ru.bardinpetr.itmo.lab5.models.fields.Field;

public interface UserPromptedAPICommand {

    /**
     * Get textual identifier of command
     */
    String getType();

    /**
     * list for arguments which should be entered in line with command
     */
    default Field<?>[] getInlineArgs() {
        return new Field[0];
    }

    /**
     * list for arguments which should be entered line by line
     */
    default Field<?>[] getInteractArgs() {
        return new Field[0];
    }

}


