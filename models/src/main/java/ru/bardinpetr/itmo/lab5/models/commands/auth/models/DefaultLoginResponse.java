package ru.bardinpetr.itmo.lab5.models.commands.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultLoginResponse extends LoginResponse {
    private String username;
    private Integer userId;
    private String role;
}
