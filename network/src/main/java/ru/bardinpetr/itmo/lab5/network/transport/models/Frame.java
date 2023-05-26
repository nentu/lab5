package ru.bardinpetr.itmo.lab5.network.transport.models;

import lombok.Data;
import ru.bardinpetr.itmo.lab5.network.transport.errors.SizeLimitException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.SessionFrame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

@Data
public class Frame {
    public static final long INVALID_ID = -1;
    public static final long FIRST_ID = 0;
    public static int HEADER_SIZE = (Long.SIZE + Integer.SIZE) / 8;
    public static int PAYLOAD_SIZE = 1024 - HEADER_SIZE;
    public static int MAX_SIZE = HEADER_SIZE + PAYLOAD_SIZE;
    private final long id;
    private final int currentPayloadSize;
    private final byte[] payload;

    public Frame(long id, byte[] data) {
        if (data.length > (PAYLOAD_SIZE)) throw new SizeLimitException("Frame limit");
        this.id = id;
        this.payload = data;
        currentPayloadSize = data.length;
    }

    public Frame(long id) {
        this(id, new byte[0]);
    }

    public static Frame fromChannel(ReadableByteChannel channel) throws IOException {
        var buffer = ByteBuffer.allocate(Frame.MAX_SIZE);
        channel.read(buffer);
        return SessionFrame.fromBytes(buffer.array());
    }

    public static Frame fromBytes(byte[] bytes) {
        var byteChannel = ByteBuffer.wrap(bytes);
        int payloadSize = byteChannel.getInt();
        long id = byteChannel.getLong();
        byte[] payload = new byte[payloadSize];
        byteChannel.get(payload);

        return new Frame(id, payload);
    }

    public static void argueWithOlga(long currentId, long excepted) {
        throw new TransportException("Failed to receive ACK from server. Probably very high load. Current %d, expected %d".formatted(currentId, excepted));
    }

    public void checkACK(Frame frame) throws TransportException {
        if (frame.id != id) {
            argueWithOlga(frame.getId(), id);
        }
    }

    public byte[] toBytes() {
        var byteChannel = ByteBuffer.allocate(HEADER_SIZE + getPayload().length);
        byteChannel.putInt(getCurrentPayloadSize());
        byteChannel.putLong(getId());
        byteChannel.put(getPayload());
        return byteChannel.array();
    }

}
