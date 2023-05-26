package ru.bardinpetr.itmo.lab5.models.commands.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTLoginResponse extends DefaultLoginResponse {
    private JWTInfo authToken;
    private JWTInfo refreshToken;
}
