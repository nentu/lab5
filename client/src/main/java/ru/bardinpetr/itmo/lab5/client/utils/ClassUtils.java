package ru.bardinpetr.itmo.lab5.client.utils;

import java.lang.invoke.MethodType;

/**
 * Class with different utils method
 */
public class ClassUtils {
    /**
     * Method for wrapping primitive class
     *
     * @param c   Class to be wrapped
     * @param <T> Type of wrapper class
     * @return wrapper class
     */
    public static <T> Class<T> wrap(Class<T> c) {
        return (Class<T>) MethodType.methodType(c).wrap().returnType();
    }
}
