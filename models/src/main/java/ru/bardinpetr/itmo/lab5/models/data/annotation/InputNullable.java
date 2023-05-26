package ru.bardinpetr.itmo.lab5.models.data.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark annotation for selecting nullable fields
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InputNullable {
}
