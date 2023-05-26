package ru.bardinpetr.itmo.lab5.models.fields;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;
import ru.bardinpetr.itmo.lab5.models.validation.IValidator;

/**
 * Class for full field description
 *
 * @param <T>
 */
@Getter
@ToString
@EqualsAndHashCode
public class FieldWithDesc<T> extends Field<T> {
    private final String promptMsg;
    private final IValidator<T> validator;
    private final boolean nullAble;

    public FieldWithDesc(String name, Class<T> kClass, String requestMsg, IValidator<T> validator, boolean nullAble) {
        super(name, kClass);
        this.promptMsg = requestMsg;
        this.validator = (validator == null)
                ? s -> new ValidationResponse(true, "")
                : validator;

        this.nullAble = nullAble;
    }
}