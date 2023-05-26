package ru.bardinpetr.itmo.lab5.models.data.annotation;

import ru.bardinpetr.itmo.lab5.models.data.validation.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for getting validator class where should be a validation method of field with such name validate(field name with first capital letter)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValidator {
    Class<? extends Validator> value();
}