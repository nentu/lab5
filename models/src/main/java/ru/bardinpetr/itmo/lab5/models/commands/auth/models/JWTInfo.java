package ru.bardinpetr.itmo.lab5.models.commands.auth.models;

import java.time.ZonedDateTime;

public record JWTInfo(String token, ZonedDateTime expiration) {
}
