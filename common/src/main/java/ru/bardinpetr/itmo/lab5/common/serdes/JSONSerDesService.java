package ru.bardinpetr.itmo.lab5.common.serdes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
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
public class JSONSerDesService<T> extends SerDesService<T> {

    public JSONSerDesService(Class<T> baseClass) {
        super(baseClass);
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        var mapper = new JsonMapper();

        var formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        var timeModule =
                new JavaTimeModule()
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));

        mapper.setSerializationInclusion(JsonInclude.Include.CUSTOM);

        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.activateDefaultTyping(
                getPolymorphicValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        mapper.registerModule(timeModule);
        return mapper;
    }

    protected BasicPolymorphicTypeValidator getPolymorphicValidator() {
        return BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType("ru.bardinpetr.itmo.lab5")
                .allowIfSubTypeIsArray()
                .allowIfSubType("java")
                .build();
    }
}
