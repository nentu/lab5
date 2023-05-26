package ru.bardinpetr.itmo.lab5.network;

import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;
import ru.bardinpetr.itmo.lab5.network.transport.server.UDPServerFactory;

import java.io.IOException;
import java.net.SocketAddress;


public class Main {
    public static void main(String[] args) throws IOException {
//        SetupJUL.loadProperties(Main.class);


        IServerTransport<SocketAddress, SocketMessage> server = UDPServerFactory.create(1856);

        server.subscribe((sender, message) -> {
            var msg = new SocketMessage(
                    SocketMessage.CommandType.ACK,
                    message.getId(),
                    message.getId() + 1,
                    "Artem".repeat(1201).getBytes()
            );
            message.setCmdType(SocketMessage.CommandType.ACK);
            server.send(sender, msg);
        });


//        server.run();
    }
}
/*
package ru.bardinpetr.itmo.lab5.network.app.client;

import ru.bardinpetr.itmo.lab5.network.transport.client.UDPClientTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

public class ClientMain {
    private static final UDPClientTransport client = new UDPClientTransport(new InetSocketAddress("localhost", 1249));

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        var t1 = new SocketMessage(SocketMessage.CommandType.DATA, 123L, 2314L, "Artem the best".repeat(500).getBytes());


        for (int i = 0; i < 100; i++) {
            var thr = new Thread(() -> {
                try {
                    clientRun(t1);
                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            });
            thr.run();
        }

    }

    public static void clientRun(SocketMessage t1) throws IOException, TimeoutException {
        SocketMessage msg;
        String t;
        while (true) {
            client.send(t1);
            msg = client.receive(Duration.ofSeconds(2));

            String payload = new String(msg.getPayload());
            System.out.println(
                    msg.getId()+" "+
                            msg.getCmdType()+" "+
                            payload.length()
//                        +" "+payload
            );
        }
    }
}

 */
