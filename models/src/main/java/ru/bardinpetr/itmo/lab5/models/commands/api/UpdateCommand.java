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
 * Class of update command
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UpdateCommand extends UserAPICommand {
    @NonNull
    public Integer id;
    @NonNull
    public Worker element;

    @Override
    public String getType() {
        return "update";
    }

    @Override
    public ValidationResponse validate() {
        return (new WorkerValidator()).validateAll(element);
    }

    @Override
    public Field<?>[] getInlineArgs() {
        return new Field[]{
                new Field<>("id", Integer.class)
        };
    }

    @Override
    public Field<?>[] getInteractArgs() {
        return new Field[]{
                new Field<>("element", Worker.class)
        };
    }
}
