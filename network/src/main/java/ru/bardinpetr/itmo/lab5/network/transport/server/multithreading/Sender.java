package ru.bardinpetr.itmo.lab5.network.transport.server.multithreading;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.Session;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.SessionFrame;
import ru.bardinpetr.itmo.lab5.network.utils.TransportUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Map;

@Slf4j
public class Sender extends Thread {
    private final DatagramChannel channel;
    private final Session session;
    private final SocketMessage message;
    private final Map<Integer, Session> clientsMap;
    JSONSerDesService<SocketMessage> serDesService = new JSONSerDesService<>(SocketMessage.class);

    public Sender(
            Session session,
            DatagramChannel channel,
            SocketMessage message,
            Map<Integer, Session> map) {
        this.channel = channel;
        this.message = message;
        this.clientsMap = map;
        this.session = session;
    }

    @Override
    public void run() {  //компот
//        System.out.println("C");
        try {
            log.info("Start sending in %d session".formatted(session.getId()));
            var frameList = TransportUtils.separateBytes(
                    session.getId(),
                    serDesService.serialize(message),
                    1
            );
            var lenInBytes = TransportUtils.IntToBytes(frameList.size());
            channel.send(
                    packBytesToFrame(
                            0,
                            lenInBytes.array()
                    ), session.getAddress());

            receiveAndCheck(Frame.FIRST_ID);

            for (int i = 0; i < frameList.size(); i++) {
                channel.send(ByteBuffer.wrap(frameList.get(i).toBytes()), session.getAddress());
                receiveAndCheck(i + 2);
            }

            log.info("Send message to " + session.getAddress());

        } catch (SerDesException ignored) {
        } catch (IOException e) {
            log.info("Sending frame exception");
            throw new TransportException(e);
        } finally {
            closeSession();
            log.info("finish sending");
//            System.out.println("D");
        }
    }

    private void closeSession() {
        try {
            log.info("Close session %d".formatted(session.getId()));
            clientsMap.remove(session.getId());
            session.getPipe().sink().close();
            session.getPipe().source().close();

        } catch (IOException e) {
            log.error("Error during closing receiving session", e);
            throw new TransportException(e);
        }
    }

    private void receiveAndCheck(long id) throws IOException {
        var frame = SessionFrame.fromChannel(session.getPipe().source());
        if (frame.getId() != id) {
            Frame.argueWithOlga(frame.getId(), id);
        }

    }

    private ByteBuffer packBytesToFrame(int id, byte[] bytes) {
        return ByteBuffer.wrap(new SessionFrame(
                        session.getId(),
                        id,
                        bytes,
                1
                ).toBytes()
        );
    }
}
