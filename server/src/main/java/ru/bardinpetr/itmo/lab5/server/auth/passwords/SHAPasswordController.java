package ru.bardinpetr.itmo.lab5.server.auth.passwords;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class SHAPasswordController implements IPasswordController {
    private final String pepper = "S#F_A_P_@k";
    private final MessageDigest md;

    public SHAPasswordController() {
        try {
            this.md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String randomString(int length) {
        return new Random().ints(length, 33, 126).
                mapToObj(x -> new String(new byte[]{(byte) x})).collect(Collectors.joining(""));
    }

    @Override
    public byte[] getHash(String password, String salt) {
        byte[] hash = md.digest(
                (pepper + password + salt).getBytes(StandardCharsets.UTF_8));

        return hash;


    }

    @Override
    public boolean compare(byte[] first, byte[] second) {
        return Arrays.equals(first, second);
    }

}
