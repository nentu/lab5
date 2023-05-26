package ru.bardinpetr.itmo.lab5.network.transport.server.multithreading;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.handlers.IMessageHandler;
import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.Session;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.SessionFrame;
import ru.bardinpetr.itmo.lab5.network.utils.TransportUtils;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

/**
 * Class for receiving session
 */
@Slf4j
public class Receiver extends RecursiveAction {
    private final Pipe.SourceChannel pipeSource;
    private final DatagramChannel channel;
    private final Session session;
    private final IMessageHandler<SocketAddress, SocketMessage> handler;
    private final Map<Integer, Session> clientsMap;
    private boolean sessionClosed = false;
    JSONSerDesService<SocketMessage> serDesService = new JSONSerDesService<>(SocketMessage.class);


    public Receiver(Session session, DatagramChannel channel, IMessageHandler<SocketAddress, SocketMessage> handler, Map<Integer, Session> map) {
        this.handler = handler;
        this.session = session;
        this.channel = channel;
        this.pipeSource = session.getPipe().source();
        clientsMap = map;
    }

    /**
     * Send frame with id
     *
     * @param frameId
     * @throws IOException
     */
    private void respondToFrame(int frameId) throws IOException {
        channel.send(
                ByteBuffer.wrap(
                        new SessionFrame(session.getId(), frameId, 0).toBytes()
                ),
                session.getAddress());
    }

    /**
     * Main method for receiving socket messages and starting handler
     */
    @Override
    protected void compute() {
//        System.out.println("A");
        log.info("Start receiving message is %d session".formatted(session.getId()));

        try {
            var initFrame = SessionFrame.fromChannel(pipeSource);
            if (initFrame.getId() != Frame.FIRST_ID) {
                Frame.argueWithOlga(initFrame.getId(), Frame.FIRST_ID);
            }
            respondToFrame(0);
            int len = ByteBuffer.wrap(initFrame.getPayload()).getInt();

            log.info("Got new socket message with frames list size: " + len);
            ArrayList<Frame> receiveList = new ArrayList<>(len);

            for (int i = 0; i < len; i++) {
                var frame = SessionFrame.fromChannel(pipeSource);
                if (frame.getId() != i + 2) {
                    Frame.argueWithOlga(frame.getId(), i + 2);
                }
                receiveList.add(
                        frame
                );
                respondToFrame(i + 2);
            }

            var desBytes = TransportUtils.joinFrames(receiveList);
            SocketMessage tmpMsg;
            try {
                tmpMsg = serDesService.deserialize(desBytes);      //dived to threads
            } catch (SerDesException e) {
                tmpMsg = new SocketMessage(new byte[]{});
            }
            log.info("Deserialized frames to socket message");
            log.info("End receiving in %d session".formatted(session.getId()));
            closeSession();
            SocketMessage msg = tmpMsg;

            var thread = new Thread(() -> {
                handler.handle(session.getAddress(), msg);
            });
            thread.start();
//            thread.join();
        } catch (Exception e) {
            log.error("Error during receiving session", e);
            closeSession();
            throw new TransportException(e);
        }
    }

    /**
     * Close session
     */
    private void closeSession() {
        if (sessionClosed) return;
        try {
            clientsMap.remove(session.getId());
            session.getPipe().sink().close();
            session.getPipe().source().close();
            sessionClosed = true;
        } catch (IOException e) {
            log.error("Error during closing receiving session", e);
            throw new TransportException(e);
        }
    }


}
