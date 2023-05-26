package ru.bardinpetr.itmo.lab5.client.api.description;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.client.api.connectors.APIProvider;
import ru.bardinpetr.itmo.lab5.client.utils.ClassUtils;
import ru.bardinpetr.itmo.lab5.client.utils.StringUtils;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.api.GetOrgsCommand;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.annotation.FieldValidator;
import ru.bardinpetr.itmo.lab5.models.data.annotation.InputNullable;
import ru.bardinpetr.itmo.lab5.models.data.annotation.InteractText;
import ru.bardinpetr.itmo.lab5.models.data.annotation.NotPromptRequired;
import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;
import ru.bardinpetr.itmo.lab5.models.data.validation.Validator;
import ru.bardinpetr.itmo.lab5.models.fields.FieldWithDesc;
import ru.bardinpetr.itmo.lab5.models.validation.IValidator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for describing data class's fields with their name, type, prompt message and validation function
 * <p>
 * In constructor you need just to specify root classes and their field's classes will be registered with recursion
 */
@Data
public class APICommandsDescriptionHolder {
    /**
     * Main map with all information for each field: name, type, validation function,
     * nullAble boolean and text for interact
     */
    private Map<Class<?>, List<FieldWithDesc<?>>> dataDescriptions = new HashMap<>();

    public APICommandsDescriptionHolder(Class<?>[] listOfClasses) {
        for (var i : listOfClasses) {
            addToMap(i);
        }
    }

    public static <T> IValidator<T> getValidator(Class<? extends Validator> validatorClass, Field field, Class<?> addedClass) {
        if (addedClass.equals(Organization.class) && field.getName().equals("id")) {
            return s -> {
                var p = APIProvider.getConnector();
                try {
                    var res = (GetOrgsCommand.OrganisationCommandResponse) p.call(new GetOrgsCommand());
                    for (var i : res.getOrganizations()) {
                        if (i.getId().equals(s)) return new ValidationResponse(true, "");
                    }
                    return new ValidationResponse(false, "Unknown id");
                } catch (APIClientException e) {
                    return new ValidationResponse(false, "Unknown id");
                }

            };
        }
        try {
            var method = validatorClass.getMethod(
                    "validate%s".formatted(
                            StringUtils.capitalize(field.getName())
                    ),
                    ClassUtils.wrap(field.getType())
            );
            var constr = validatorClass.getConstructor().newInstance();
            return s -> {
                try {
                    return (ValidationResponse) method.invoke(constr, new Object[]{s});
                } catch (IllegalAccessException e) {
                    throw new Error("method access: " + e);
                }
            };
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            return s -> new ValidationResponse(true, "Auto create");
        } catch (InstantiationException e) {
            throw new Error("Constructor: " + e);
        } catch (IllegalAccessException e) {
            throw new Error("Access: " + e);
        }
    }

    /**
     * method for recurrent class registration
     */

    private void addToMap(Class<?> addedClass) {
        this.dataDescriptions.put(addedClass, null);

        var fieldsList = new ArrayList<FieldWithDesc<?>>();
        for (var field : addedClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(NotPromptRequired.class)) {
                if (field.getType().getPackage() == addedClass.getPackage() && (!field.getType().isEnum()) && (!dataDescriptions.containsKey(field.getType())))
                    addToMap(field.getType());

                var validatorAnnotation = field.getAnnotation(FieldValidator.class);

                fieldsList.add(new FieldWithDesc<>(
                        field.getName(),
                        ClassUtils.wrap(field.getType()),
                        field.getAnnotation(InteractText.class).value(),
                        validatorAnnotation == null ? null : getValidator(validatorAnnotation.value(), field, addedClass),
                        (!(field.getClass().isPrimitive()) && field.isAnnotationPresent(InputNullable.class))
                ));
            }
        }
        this.dataDescriptions.put(addedClass, fieldsList);
    }
}
