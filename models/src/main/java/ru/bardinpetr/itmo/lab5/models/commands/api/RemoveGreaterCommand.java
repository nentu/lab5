package ru.bardinpetr.itmo.lab5.models.commands.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;
import ru.bardinpetr.itmo.lab5.models.data.validation.WorkerValidator;
import ru.bardinpetr.itmo.lab5.models.fields.Field;

/**
 * Class of remove_greater command
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RemoveGreaterCommand extends UserAPICommand {
    @NonNull
    public Worker element;

    @Override
    public ValidationResponse validate() {
        return (new WorkerValidator()).validateAll(element);
    }

    @Override
    public String getType() {
        return "remove_greater";
    }

    @Override
    public Field<?>[] getInteractArgs() {
        return new Field[]{
                new Field<>("element", Worker.class)
        };
    }

}
