package ru.bardinpetr.itmo.lab5.common.serdes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SerDesException extends Exception {
    private Type direction;
    private Exception internalException;

    public enum Type {
        SERIALIZE, DESERIALIZE
    }
}
