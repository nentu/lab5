package ru.bardinpetr.itmo.lab5.client.ui.cli.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import ru.bardinpetr.itmo.lab5.client.api.description.APICommandsDescriptionHolder;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.errors.ParserException;
import ru.bardinpetr.itmo.lab5.common.serdes.ObjectMapperFactory;
import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;
import ru.bardinpetr.itmo.lab5.models.fields.FieldWithDesc;
import ru.bardinpetr.itmo.lab5.models.validation.IValidator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class for interacting data objects
 */
public class ObjectScanner {
    private final Map<Class<?>, List<FieldWithDesc<?>>> dataDescription;
    private final Scanner scaner;
    private final ConsolePrinter printer;
    private final ObjectMapper mapper = ObjectMapperFactory.createMapper();

    public ObjectScanner(APICommandsDescriptionHolder dataDescription, ConsolePrinter printer, Scanner scanner) {
        this.dataDescription = dataDescription.getDataDescriptions();
        this.printer = printer;
        this.scaner = scanner;
    }

    private String scan() {
        String string = "";
        string = scaner.nextLine();

        return string.isEmpty() ? null : string;
    }

    /**
     * Method for single value interaction
     *
     * @param kClass - Class of value
     * @param <T>    Type of value
     * @return object if required type
     */
    private <T> ScannerRespond<T> interactValue(Class<T> kClass, T defaultValue, List<String> blacklist) throws ParserException, RuntimeException {
        try {
            if (!dataDescription.containsKey(kClass))
                return new ScannerRespond<>(mapper.convertValue(scan(), kClass), 0);
            else
                return scan(kClass, defaultValue, blacklist);
        } catch (IllegalArgumentException e) {
            throw new ParserException("Invalid argument");
        }
    }

    /**
     * Method for integrating thw whole object
     *
     * @param kClass class of scanned object
     * @param <T>    class of scanned object
     * @return fulfilled object
     */
    public <T> ScannerRespond<T> scan(Class<T> kClass, T defaultObject, List<String> blacklist) throws ParserException, NoSuchElementException {
        var defaultObjectMap = mapper.convertValue(defaultObject, HashMap.class);

        Map<String, Object> objectMap = new HashMap<>();
        List<FieldWithDesc<?>> fields = dataDescription.get(kClass);
        if (fields == null) throw new ParserException(kClass.getName());

        int countOfRepeat = 0;

        for (int i = 0; i < fields.size(); i++) {
            var cur = fields.get(i);
            if (blacklist.contains(cur.getName())) continue;

            Object curDefaultVar;
            printer.display(cur.getPromptMsg());
            if (defaultObjectMap != null) {
                curDefaultVar = defaultObjectMap.get(cur.getName());
                String str;
                if (curDefaultVar != null)
                    str = curDefaultVar.toString();
                else
                    str = "null";

                printer.displayInLine(String.format("Default is \"%s\". ", str));
                printer.display("Press N to enter a new value, or press Enter to continue with default one.");
                String resp = scaner.nextLine();
                if (resp.equals("")) {
                    objectMap.put(cur.getName(), curDefaultVar);
                    printer.display("Default value was used");
                    continue;
                }
                if (!resp.equals("N")) {
                    printer.display("Invalid interact");
                    i--;
                    continue;
                }
                //printer.display(cur.getPromptMsg());
            } else {
                curDefaultVar = null;
            }

            while (enterField(cur, objectMap, curDefaultVar, blacklist) == 1) {
                if (!scaner.hasNextLine()) throw new ParserException("Not enough lines in script");
                countOfRepeat++;
            }
        }
        return new ScannerRespond<>(mapper.convertValue(objectMap, kClass), countOfRepeat);
    }

    /**
     * enteraction single field
     *
     * @param cur           current field
     * @param objectMap     Map of building object
     * @param curDefaultVar default value of current value
     * @param <T>           type of field
     * @return completed value field
     * @throws ParserException
     */
    private <T> int enterField(FieldWithDesc<T> cur, Map<String, Object> objectMap, Object curDefaultVar, List<String> blacklist) throws ParserException {
        if (blacklist.contains(cur.getName()))
            return 0;

        printer.display("-> " + cur.getName() + " interaction");
        if (cur.isNullAble()) {
            printer.display("If object does not exist press Enter. To continue interaction enter C");
            if (!scaner.hasNextLine()) return 1;
            String answer = scaner.nextLine();
            if (answer.equals("")) {
                objectMap.put(cur.getName(), null);
                return 0;
            }
            if (!answer.equals("C")) {
                printer.display("Invalid choice");
                //printer.display(cur.getPromptMsg());
                return 1;
            }
            //printer.display("Continue interaction");
            printer.display(cur.getPromptMsg());
        }


        Object value = null;
        try {
            var cls = cur.getValueClass();
            value = interactValue(cls, mapper.convertValue(curDefaultVar, cls), blacklist).object;
        } catch (ParserException ex) {
            printer.display("Invalid argument");
            printer.display(cur.getPromptMsg());
            return 1;
        } catch (RuntimeException ex) {
            throw new ParserException(ex.getMessage());
        }
        if ((!cur.isNullAble()) && value == null) {
            printer.display("Invalid argument: Argument can't be null");

            return 1;
        }

        IValidator val = cur.getValidator();
        ValidationResponse res = null;
        try {
            res = val.validate(cur.getValueClass().cast(value));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (res.isAllowed()) {
            objectMap.put(cur.getName(), value);
        } else {
            printer.display("Invalid argument: " + res.getMsg());
            return 1;
        }
        return 0;
    }

    /**
     * Object Scanner response class with built object and count of iterations
     *
     * @param <T> Type of respond object
     */
    @Data
    public static class ScannerRespond<T> {
        public final T object;
        public final int countOfRepeat;
    }
}
