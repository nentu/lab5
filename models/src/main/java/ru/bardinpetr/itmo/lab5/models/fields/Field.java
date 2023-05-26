package ru.bardinpetr.itmo.lab5.models.fields;

import lombok.Data;

/**
 * Class for Field base description
 */
@Data
public class Field<T> {
    private String name;
    private Class<T> valueClass;

    public Field() {
    }

    public Field(String name, Class<T> kClass) {
        this.name = name;
        this.valueClass = kClass;
    }
}
