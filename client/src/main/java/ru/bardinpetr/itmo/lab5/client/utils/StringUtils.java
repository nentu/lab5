package ru.bardinpetr.itmo.lab5.client.utils;

/**
 * Class with different string utils
 */
public class StringUtils {
    /**
     * Make first letter capital
     *
     * @param inputString String where should capitalize first letter
     * @return Changed string with capital first letter
     */
    public static String capitalize(String inputString) {
        char firstLetter = inputString.charAt(0);
        char capitalFirstLetter = Character.toUpperCase(firstLetter);
        return capitalFirstLetter + inputString.substring(1);
    }
}