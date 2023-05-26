package ru.bardinpetr.itmo.lab5.common.serdes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides functions for serializing and deserializing POJO to XML based on jackson.dataformat.xml
 *
 * @param <T> Base type to operate on
 */
public class XMLSerDesService<T> extends SerDesService<T> {

    public XMLSerDesService(Class<T> baseClass) {
        super(baseClass);
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        var mapper = new XmlMapper();

        var formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        var timeModule =
                new JavaTimeModule()
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.registerModule(timeModule);
        return mapper;
    }
}
