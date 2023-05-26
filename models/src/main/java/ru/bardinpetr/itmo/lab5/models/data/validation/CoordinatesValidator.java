package ru.bardinpetr.itmo.lab5.models.data.validation;

import ru.bardinpetr.itmo.lab5.models.data.Coordinates;

/**
 * A class for coordinate's data validation
 */
public class CoordinatesValidator implements Validator {
    /**
     * @param x x coordinate
     * @return response with error message
     */
    public static ValidationResponse validateX(Integer x) {
        if (x == null) return new ValidationResponse(false, "x coordinate can't be null");
        return (x >= 773) ?
                new ValidationResponse(false, "X coordinate must be less than 773") :
                new ValidationResponse(true, "");
    }

    public static ValidationResponse validateY(Float y) {
        if (y == null) return new ValidationResponse(false, "y coordinate can't be null");
        return (y <= -413) ?
                new ValidationResponse(false, "Y coordinate must be greater than -413") :
                new ValidationResponse(true, "");
    }


    /**
     * @param coordinates Coordinates object
     * @return response with error message
     */
    public static ValidationResponse validateAll(Coordinates coordinates) {
        if (!validateX(coordinates.getX()).isAllowed()) return validateX(coordinates.getX());
        if (!validateY(coordinates.getY()).isAllowed()) return validateY(coordinates.getY());
        return new ValidationResponse(true, "");
    }
}
