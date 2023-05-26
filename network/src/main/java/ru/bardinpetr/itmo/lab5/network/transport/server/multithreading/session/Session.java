package ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session;

import lombok.Data;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Pipe;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Session {
    public static AtomicInteger nextId = new AtomicInteger(0);
    private final int id;
    private final Pipe pipe;
    private final SocketAddress address;

    public Session(SocketAddress address) throws IOException {
        this.pipe = Pipe.open();
        this.address = address;
        id = nextId.getAndIncrement();
    }
}
