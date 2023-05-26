package ru.bardinpetr.itmo.lab5.server.app.modules.auth;

import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.JWTAPICommandAuthenticator;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.JWTAuthenticationApplication;
import ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.storage.JWTHMACKeyProvider;
import ru.bardinpetr.itmo.lab5.server.auth.recv.DBAuthenticationReceiver;
import ru.bardinpetr.itmo.lab5.server.db.dao.DBTableProvider;

import static ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.JWTAuthenticationReceiverAdapter.KID_ACCESS;
import static ru.bardinpetr.itmo.lab5.network.app.server.modules.auth.app.jwt.JWTAuthenticationReceiverAdapter.KID_REFRESH;

public class AuthAppFacade {

    public static JWTAuthenticationApplication create(DBTableProvider tableProvider) {
        var authReceiver = new DBAuthenticationReceiver(tableProvider.getUsers());

        var jwtKeyProvider = new JWTHMACKeyProvider();
        jwtKeyProvider.registerGenerate(KID_ACCESS);
        jwtKeyProvider.registerGenerate(KID_REFRESH);

        return new JWTAuthenticationApplication(
                JWTAPICommandAuthenticator.getInstance(),
                authReceiver,
                jwtKeyProvider
        );
    }
}

