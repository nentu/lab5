package ru.bardinpetr.itmo.lab5.models.validation;

import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface IValidator<T> {
    ValidationResponse validate(T s) throws InvocationTargetException;
}
