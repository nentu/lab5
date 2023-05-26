package ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session;

import lombok.Getter;
import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

@Getter
public class SessionFrame extends Frame {
    public static int MAX_SIZE = 1024;
    public static int HEADER_SIZE = (Long.SIZE + Integer.SIZE + Integer.SIZE+Integer.SIZE) / 8;
    public static int PAYLOAD_SIZE = MAX_SIZE - HEADER_SIZE;
    private final int sessionId;
    private final int sending;

    public SessionFrame(int sessionId, long id, byte[] data, int sending) {
        super(id, data);
        this.sessionId = sessionId;
        this.sending = sending;
    }

    public SessionFrame(int sessionId, long id, int sending) {
        this(sessionId, id, new byte[0], sending);
    }

    public static SessionFrame fromChannel(ReadableByteChannel channel) throws IOException {
        var buffer = ByteBuffer.allocate(Frame.MAX_SIZE);
        channel.read(buffer);
        return SessionFrame.fromBytes(buffer.array());
    }

    public static SessionFrame fromBytes(byte[] bytes) {
        var byteChannel = ByteBuffer.wrap(bytes);
        int payloadSize = byteChannel.getInt();
        int sessionId = byteChannel.getInt();
        int sending = byteChannel.getInt();
        long id = byteChannel.getLong();
        byte[] payload = new byte[payloadSize];
        byteChannel.get(payload);

        return new SessionFrame(sessionId, id, payload, sending);
    }

    public byte[] toBytes() {
        var byteChannel = ByteBuffer.allocate(HEADER_SIZE + getPayload().length);
        byteChannel.putInt(getCurrentPayloadSize());
        byteChannel.putInt(sessionId);
        byteChannel.putInt(sending);
        byteChannel.putLong(getId());
        byteChannel.put(getPayload());
        return byteChannel.array();
    }
}
