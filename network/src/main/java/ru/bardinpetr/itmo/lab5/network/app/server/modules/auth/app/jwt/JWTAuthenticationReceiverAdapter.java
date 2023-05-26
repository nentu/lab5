package ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import ru.bardinpetr.itmo.lab5.models.commands.auth.LoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.PasswordLoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.RefreshLoginCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.RegisterCommand;
import ru.bardinpetr.itmo.lab5.models.commands.auth.models.*;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.storage.JWTKeyProvider;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.InvalidCredentialsException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.UserExistsException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.errors.UserNotFoundException;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.models.Authentication;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.recv.AuthenticationReceiver;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JWTAuthenticationReceiverAdapter implements AuthenticationReceiver<JWTAuthenticationCredentials, JWTLoginResponse> {

    public static final Integer ACCESS_TOKEN_EXP_SECONDS = 30;
    public static final Integer REFRESH_TOKEN_EXP_SECONDS = 30 * 60;
    public static final String KID_ACCESS = "main";
    public static final String KID_REFRESH = "refresh";
    private static final String ISSUER = "srvlab5";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_ROLE = "role";

    private final ConcurrentMap<String, String> usedUUID = new ConcurrentHashMap<>();
    private final AuthenticationReceiver<DefaultAuthenticationCredentials, DefaultLoginResponse> db;
    private final JWTKeyProvider keyProvider;
    private JwtParser decoder;

    public JWTAuthenticationReceiverAdapter(AuthenticationReceiver<DefaultAuthenticationCredentials, DefaultLoginResponse> authenticationReceiver, JWTKeyProvider keyProvider) {
        this.db = authenticationReceiver;
        this.keyProvider = keyProvider;

        this.decoder = Jwts.parserBuilder()
                .setSigningKeyResolver(keyProvider.getDecodeKeyResolver())
                .setAllowedClockSkewSeconds(1)
                .requireIssuer(ISSUER)
                .build();
    }

    @Override
    public Authentication authorize(JWTAuthenticationCredentials request) {
        try {
            decoder = Jwts.parserBuilder()
                    .setSigningKeyResolver(keyProvider.getDecodeKeyResolver())
                    .setAllowedClockSkewSeconds(1)
                    .requireIssuer(ISSUER)
                    .build();
            var jwt = decoder
                    .parseClaimsJws(request.getToken())
                    .getBody();

            return Authentication.ok(
                    Integer.parseInt(jwt.getAudience()),
                    (String) jwt.get(CLAIM_USERNAME),
                    (String) jwt.get(CLAIM_ROLE)
            );
        } catch (Exception e) {
            return Authentication.invalid();
        }
    }

    @Override
    public DefaultLoginResponse requestIdentity(int userId) {
        return db.requestIdentity(userId);
    }

    @Override
    public JWTLoginResponse login(LoginCommand request) throws UserNotFoundException, InvalidCredentialsException {
        var strategy = request.getStrategy();
        if (strategy.equals(AuthStrategy.LOGIN_PASS))
            return loginPassword((PasswordLoginCommand) request);
        else if (strategy.equals(AuthStrategy.REFRESH_TOKEN))
            return loginRefresh((RefreshLoginCommand) request);
        else
            throw new InvalidCredentialsException("Invalid strategy");
    }

    protected JWTLoginResponse loginRefresh(RefreshLoginCommand request) throws UserNotFoundException, InvalidCredentialsException {
        JWTAuthenticationCredentials creds = request.getCredentials();

        int userId;
        try {
            var jwt = decoder
                    .parseClaimsJws(creds.getToken())
                    .getBody();

            if (usedUUID.put(jwt.getId(), "") != null)
                throw new InvalidCredentialsException("Token reusage detected");

            userId = Integer.parseInt(jwt.getAudience());
        } catch (Exception e) {
            throw new InvalidCredentialsException(e);
        }

        DefaultLoginResponse loginData = db.requestIdentity(userId);

        return new JWTLoginResponse(
                createAccessToken(loginData),
                createRefreshToken(loginData)
        );
    }

    protected JWTLoginResponse loginPassword(PasswordLoginCommand request) throws UserNotFoundException, InvalidCredentialsException {
        DefaultLoginResponse loginData;
        try {
            loginData = db.login(request);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Throwable e) {
            throw new InvalidCredentialsException(e);
        }

        return new JWTLoginResponse(
                createAccessToken(loginData),
                createRefreshToken(loginData)
        );
    }

    @Override
    public JWTLoginResponse register(RegisterCommand command) throws UserExistsException, InvalidCredentialsException {
        DefaultLoginResponse loginData;
        try {
            loginData = db.register(command);
        } catch (UserExistsException e) {
            throw e;
        } catch (Throwable e) {
            throw new InvalidCredentialsException(e);
        }

        return new JWTLoginResponse(
                createAccessToken(loginData),
                createRefreshToken(loginData)
        );
    }

    protected JWTBuilderInfo createBaseToken(String kid, Integer expSeconds) {
        var exp = Calendar.getInstance();
        exp.add(Calendar.SECOND, expSeconds);
        var now = new Date();

        return new JWTBuilderInfo(
                Jwts.builder()
                        .setHeader(Map.of("kid", kid))
                        .setExpiration(exp.getTime())
                        .setIssuedAt(now)
                        .setNotBefore(now)
                        .setId(UUID.randomUUID().toString())
                        .setIssuer(ISSUER)
                        .signWith(keyProvider.resolveSigningKey(kid)),
                ZonedDateTime.ofInstant(exp.toInstant(), ZoneId.systemDefault())
        );
    }

    protected JWTInfo createAccessToken(DefaultLoginResponse userData) {
        var base = createBaseToken(KID_ACCESS, ACCESS_TOKEN_EXP_SECONDS);
        String token = base.builder()
                .setAudience(String.valueOf(userData.getUserId()))
                .claim(CLAIM_USERNAME, userData.getUsername())
                .claim(CLAIM_ROLE, userData.getRole())
                .compact();
        return new JWTInfo(
                token,
                base.exp()
        );
    }

    protected JWTInfo createRefreshToken(DefaultLoginResponse userData) {
        var base = createBaseToken(KID_REFRESH, REFRESH_TOKEN_EXP_SECONDS);
        String token = base.builder()
                .setAudience(String.valueOf(userData.getUserId()))
                .compact();
        return new JWTInfo(
                token,
                base.exp()
        );
    }
}
