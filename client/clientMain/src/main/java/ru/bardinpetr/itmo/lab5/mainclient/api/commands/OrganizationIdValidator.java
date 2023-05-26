package ru.bardinpetr.itmo.lab5.mainclient.api.commands;

import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;
import ru.bardinpetr.itmo.lab5.models.validation.IValidator;

public class OrganizationIdValidator implements IValidator<Organization> {
    @Override
    public ValidationResponse validate(Organization s) {
        return null;
    }
}
