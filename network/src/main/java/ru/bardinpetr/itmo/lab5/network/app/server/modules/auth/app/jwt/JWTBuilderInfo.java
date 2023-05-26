package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt;

import io.jsonwebtoken.JwtBuilder;

import java.time.ZonedDateTime;

public record JWTBuilderInfo(JwtBuilder builder, ZonedDateTime exp) {
}
