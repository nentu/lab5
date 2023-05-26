package ru.bardinpetr.itmo.lab5.network.utils;

import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;
import ru.bardinpetr.itmo.lab5.network.transport.server.multithreading.session.SessionFrame;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utils for working with network.
 */
public class TransportUtils {
    /**
     * Convert int to bytes
     *
     * @param i int
     * @return bytes from i
     */
    public static ByteBuffer IntToBytes(int i) {
        var buffer = ByteBuffer.allocate(4);
        return buffer.putInt(i);
    }

    /**
     * Join list for frames to byte array
     *
     * @param frameList
     * @return byte array
     */
    public static byte[] joinFrames(List<Frame> frameList) {
        List<byte[]> byteList = new ArrayList<>();
        for (var i : frameList) {
            byteList.add(i.getPayload());
        }
        return join(byteList);
    }

    public static byte[] joinSessionFrames(List<SessionFrame> frameList) {
        List<byte[]> byteList = new ArrayList<>();
        for (var i : frameList) {
            byteList.add(i.getPayload());
        }
        return join(byteList);
    }

    /**
     * @param byteList
     * @return
     */

    private static byte[] join(List<byte[]> byteList) {
        int size = byteList.stream().mapToInt((a) -> a.length).sum();

        ByteBuffer buffer = ByteBuffer.allocate(size);
        for (var i : byteList) {
            buffer.put(i);
        }
        return buffer.array();
    }

    /**
     * Separate bytes to frame List
     *
     * @param source byte array
     * @return list of Frames
     */
    public static List<Frame> separateBytes(byte[] source) {
        var byteList = separate(Frame.PAYLOAD_SIZE, source);
        List<Frame> resList = new ArrayList<>();

        for (int i = 0; i < byteList.size(); i++) {
            resList.add(new Frame(
                    i + 2,
                    byteList.get(i)
            ));
        }
        return resList;
    }

    public static List<SessionFrame> separateBytes(int sessionId, byte[] source, int sending) {
        var byteList = separate(SessionFrame.PAYLOAD_SIZE, source);
        List<SessionFrame> resList = new ArrayList<>();

        for (int i = 0; i < byteList.size(); i++) {
            resList.add(new SessionFrame(
                    sessionId,
                    i + 2,
                    byteList.get(i),
                    sending
            ));
        }
        return resList;
    }

    private static List<byte[]> separate(int payloadSize, byte[] source) {

        List<byte[]> resList = new ArrayList<>();

        int start = 0;

        for (int i = 0; i < Math.ceil(source.length / (float) payloadSize) - 1; i++) {
            resList.add(
                    Arrays.copyOfRange(source, start, (start + payloadSize))
            );
            start += payloadSize;
        }
        resList.add(
                Arrays.copyOfRange(source, start, source.length)
        );
        return resList;
    }


}
