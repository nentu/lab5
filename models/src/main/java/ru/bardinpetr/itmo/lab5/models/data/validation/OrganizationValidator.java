package ru.bardinpetr.itmo.lab5.models.data.validation;

import ru.bardinpetr.itmo.lab5.models.data.Organization;

/**
 * A class for organisation's data validation
 */
public class OrganizationValidator implements Validator {

    /**
     * @param s x coordinate
     * @return response with error message
     */
    public static ValidationResponse validateFullName(String s) {
        if (s == null) {
            return new ValidationResponse(false, "Can't be null");
        } else {
            return new ValidationResponse(true, "");
        }
    }


    /**
     * @param coordinates Coordinates object
     * @return response with error message
     */
    public static ValidationResponse validateAll(Organization coordinates) {
        return validateFullName(coordinates.getFullName());
    }
}

