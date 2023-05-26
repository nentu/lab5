package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.storage;

import io.jsonwebtoken.*;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public abstract class JWTKeyProvider {
    protected final SignatureAlgorithm algorithm;
    private final Map<String, Key> keyMap = new HashMap<>();

    public JWTKeyProvider(SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void register(String kid, Key key) {
        keyMap.put(kid, key);
    }

    public Key resolveSigningKey(String kid) {
        return keyMap.get(kid);
    }

    protected Map<String, Key> getKeyMap() {
        return keyMap;
    }

    public abstract void registerGenerate(String kid);

    public SigningKeyResolver getDecodeKeyResolver() {
        return new SigningKeyResolver() {
            @Override
            public Key resolveSigningKey(JwsHeader header, Claims claims) {
                try {
                    return keyMap.get(header.getKeyId());
                } catch (Exception ignored) {
                    return null;
                }
            }

            @Override
            public Key resolveSigningKey(JwsHeader header, String plaintext) {
                throw new UnsupportedJwtException("No key selection is allowed in plaintext");
            }
        };
    }
}
