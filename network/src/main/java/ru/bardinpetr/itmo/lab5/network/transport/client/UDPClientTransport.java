package ru.bardinpetr.itmo.lab5.network.transport.client;

import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportTimeoutException;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IClientTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.Session;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.SessionFrame;
import ru.bardinpetr.itmo.lab5.network.utils.TransportUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for sending and receiving socket message  any length via UPD channel
 */
public class UDPClientTransport implements IClientTransport<SocketMessage> {
    private final DatagramSocket socket;
    private final Duration sendDurationTimeout = Duration.ofSeconds(3600); // TODO
    private final SocketAddress serverAddress;
    JSONSerDesService<SocketMessage> serDesService = new JSONSerDesService<>(SocketMessage.class);
    private int sessionId = -1;

    /**
     * @param socketAddress server address
     */
    public UDPClientTransport(SocketAddress socketAddress) {
        DatagramSocket tmp = null;
        try {
            tmp = new DatagramSocket(null);
            tmp.setReuseAddress(true);
        } catch (IOException ignore) {
        }

        socket = tmp;
        serverAddress = socketAddress;

        connect();

        disconnect();
    }

    /**
     * Inner function for receiving throw UDP channel
     *
     * @param duration timeout duration
     * @return received frame
     * @throws TransportException exception via receiving
     */
    private SessionFrame receiveFrame(Duration duration) throws TransportException, TransportTimeoutException {
        var buffer = new byte[SessionFrame.MAX_SIZE];
        var packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.setSoTimeout((int) duration.getSeconds() * 1000);
            socket.receive(packet);
        } catch (SocketTimeoutException timeoutException) {
            throw new TransportTimeoutException();
        } catch (IOException connectExc) {
            throw new TransportException();
        }

        return SessionFrame.fromBytes(packet.getData());
    }

    private void connect() throws TransportException {
        try {
            socket.connect(serverAddress);
        } catch (Exception ignored) {
            throw new TransportException(ignored);
        }
    }

    private void disconnect() {
        socket.disconnect();
    }

    private void sendFrame(SessionFrame frame) throws TransportException {
        try {
            socket.send(new DatagramPacket(frame.toBytes(), frame.toBytes().length, serverAddress));
        } catch (IOException e) {
            throw new TransportException(e);
        }
    }

    /**
     * Method for sending socket message any length.
     *
     * @param msg message to be sent
     */
    @Override
    public void send(SocketMessage msg) throws TransportException, TransportTimeoutException {
        connect();

        byte[] msgBytes;
        try {
            msgBytes = serDesService.serialize(msg);
        } catch (SerDesException e) {
            throw new TransportException("Could not serialize message");
        }


        int msgSize = (int) Math.ceil(msgBytes.length / (float) Frame.PAYLOAD_SIZE);

        var buffer = TransportUtils.IntToBytes(msgSize);

        var header = new SessionFrame(
                -1,
                Frame.FIRST_ID,
                buffer.array(), 1);

        sendFrame(header);
        var checkFrame = receiveFrame(sendDurationTimeout);
        sessionId = checkFrame.getSessionId();
        header.checkACK(checkFrame);

        var packetsList = TransportUtils.separateBytes(sessionId, msgBytes, 1);
        for (SessionFrame tmpFrame : packetsList) {
            sendFrame(tmpFrame);
            tmpFrame.checkACK(receiveFrame(sendDurationTimeout));
        }
        disconnect();
    }

    /**
     * Receive socket message from server
     *
     * @param duration timeout or null if no timeout should be applied
     * @return received socket message
     */
    public SocketMessage receive(Duration duration) throws TransportException, TransportTimeoutException {
        // seg 1 end -> create session
        sessionId = -1;
        SessionFrame header = receiveFrame(duration);
        sessionId = header.getSessionId();
        int size = ByteBuffer.wrap(header.getPayload()).getInt();

        sendFrame(new SessionFrame(sessionId, Frame.FIRST_ID, 0));

        // seg 2 end -> wait n packets + set user lock
        List<SessionFrame> frameList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            frameList.add(receiveFrame(duration));
            sendFrame(new SessionFrame(sessionId, Frame.FIRST_ID + 2 + i, 0));
        }

        SocketMessage msg;
        try {
            msg = serDesService.deserialize(TransportUtils.joinSessionFrames(frameList));
        } catch (SerDesException e) {
            throw new TransportException("Could not serialize message");
        }

        return msg;
    }
}
