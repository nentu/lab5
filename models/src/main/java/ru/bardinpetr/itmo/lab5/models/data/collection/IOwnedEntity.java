package ru.bardinpetr.itmo.lab5.models.data.collection;

public interface IOwnedEntity {
    Integer getOwner();

    boolean setOwner(Integer owner);
}
