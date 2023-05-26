package ru.bardinpetr.itmo.lab5.common.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for converting string to other class
 */
public class ValueDeserializer {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param kClass expected class
     * @param string string to be converted
     * @param <K>
     * @return object for required class
     * @throws IllegalArgumentException
     */
    public <K> K deserialize(Class<K> kClass, String string) throws IllegalArgumentException {
        return mapper.convertValue(string, kClass);
    }
}
