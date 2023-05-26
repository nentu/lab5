package ru.bardinpetr.itmo.lab5.models.data;

public enum Position {
    CLEANER(0),
    MANAGER_OF_CLEANING(1),
    ENGINEER(2),
    LEAD_DEVELOPER(3),
    HEAD_OF_DEPARTMENT(4);

    public final int value;

    Position(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}