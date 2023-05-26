package ru.bardinpetr.itmo.lab5.server.db.dto;

import ru.bardinpetr.itmo.lab5.models.data.OrganizationType;

public record OrganizationDTO(
        Integer id,
        String fullName,
        OrganizationType type
) {
}
