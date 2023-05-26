package ru.bardinpetr.itmo.lab5.server.db.dto;

import ru.bardinpetr.itmo.lab5.models.data.Coordinates;
import ru.bardinpetr.itmo.lab5.models.data.Position;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;

public record WorkerDTO(
        Integer id,
        Integer ownerId,
        Integer organizationId,
        java.time.ZonedDateTime creationDate,
        java.util.Date startDate,
        java.time.LocalDate endDate,
        String name,
        Float salary,
        Coordinates coordinates,
        Position position,
        String ownerUsername
) implements IKeyedEntity<Integer> {
    public WorkerDTO(Worker original) {
        this(original.getId(), original.getOwner(), original.getOrganization() == null ? null : original.getOrganization().getId(),
                original.getCreationDate(), original.getStartDate(), original.getEndDate(),
                original.getName(), original.getSalary(), original.getCoordinates(), original.getPosition(), original.getOwnerUsername());
    }


    @Override
    public Integer getPrimaryKey() {
        return id;
    }
}
