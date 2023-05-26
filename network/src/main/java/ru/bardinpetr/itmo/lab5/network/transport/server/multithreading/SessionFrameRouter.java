package ru.bardinpetr.itmo.lab5.network.transport.server.multithreading;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.handlers.IMessageHandler;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.Session;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.SessionFrame;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

@Slf4j
public class SessionFrameRouter implements IServerTransport<SocketAddress, SocketMessage> {
    public final Map<Integer, Session> clientPipeMap = new ConcurrentHashMap<>();
    private final DatagramChannel channel;
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private IMessageHandler<SocketAddress, SocketMessage> handler;

    public SessionFrameRouter(DatagramChannel channel) {
        this.channel = channel;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
//                System.out.printf("\n#%d %d %d$\n", forkJoinPool.getRunningThreadCount(), forkJoinPool.getActiveThreadCount(), forkJoinPool.getPoolSize());
            }
        }).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(SessionFrame.MAX_SIZE);
                SocketAddress address = channel.receive(buffer);
                SessionFrame frame = SessionFrame.fromBytes(buffer.array());
                int currentSessionId = frame.getSessionId();

                if (frame.getId() == Frame.FIRST_ID) {
                    if (!clientPipeMap.containsKey(frame.getSessionId())) {
                        currentSessionId = regClient(address);
                    }
                    else if (frame.getSending()==1) Frame.argueWithOlga(Frame.FIRST_ID, 0);
                }

                var clientPipe = clientPipeMap.get(currentSessionId).getPipe();

                clientPipe.sink().write(ByteBuffer.wrap(frame.toBytes()));

            } catch (IOException ignored) {
            }
        }

    }

    private int regClient(SocketAddress address) throws IOException {
        log.info("Start receiving");
        var session = new Session(address);
        clientPipeMap.put(session.getId(), session);
        forkJoinPool.execute(new Receiver(
                session,
                channel,
                handler,
                clientPipeMap
        ));
        return session.getId();
    }


    @Override
    public void send(SocketAddress recipient, SocketMessage data) {
        try {
            log.info("Start sending session");
//            System.out.print("sending");
            var session = new Session(recipient);
            clientPipeMap.put(session.getId(), session);

            var thread = new Sender(
                    session,
                    channel,
                    data,
                    clientPipeMap
            );

            thread.start();
        } catch (IOException e) {
            throw new TransportException(e);
        }

    }

    @Override
    public void subscribe(IMessageHandler<SocketAddress, SocketMessage> handler) {
        this.handler = handler;
    }


}
