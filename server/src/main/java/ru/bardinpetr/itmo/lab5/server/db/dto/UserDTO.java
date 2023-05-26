package ru.bardinpetr.itmo.lab5.server.db.dto;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.server.auth.passwords.IPasswordController;
import ru.bardinpetr.itmo.lab5.server.auth.passwords.SHAPasswordController;

import java.util.Arrays;


// TODO implement roles
@Data
public class UserDTO {
    private static IPasswordController pc = new SHAPasswordController();
    private final String username;
    private final byte[] hashedPassword;
    private final String salt;
    private int id = 0;

    public UserDTO(int id, String username, byte[] hashedPassword, String salt) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        salt = pc.randomString(7);
        hashedPassword = pc.getHash(password, salt);
    }

    public boolean validate() {
        return getSalt().length() <= 50 && getUsername().length() <= 50;
    }

    @Override
    public String toString() {
        return "AuthorizationObject{" +
                "username='" + username + '\'' +
                ", hashedPassword=" + Arrays.toString(hashedPassword) +
                ", salt='" + salt + '\'' +
                '}';
    }
}
