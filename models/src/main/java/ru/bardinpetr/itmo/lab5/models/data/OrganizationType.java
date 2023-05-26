package ru.bardinpetr.itmo.lab5.models.data;

public enum OrganizationType {
    COMMERCIAL(0), PUBLIC(1), PRIVATE_LIMITED_COMPANY(2), OPEN_JOINT_STOCK_COMPANY(3);

    private final int value;

    OrganizationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}