package ru.bardinpetr.itmo.lab5.network.transport.models;

import lombok.Data;

import java.net.SocketAddress;
import java.nio.channels.Pipe;
import java.util.List;

@Data
public class TransportSession {
    private Status status;
    private Frame[] receiveFrameList;

    private List<Frame> sendFrameList;

    private SocketAddress consumerAddress;
    private Pipe pipe;

    public TransportSession(SocketAddress consumerAddress, Status status, Pipe pipe) {
        this.pipe = pipe;
        this.consumerAddress = consumerAddress;
        this.status = status;
    }

    public TransportSession(SocketAddress consumerAddress, Status status, Pipe pipe, List<Frame> sendList) {
        this(consumerAddress, status, pipe);
        sendFrameList = sendList;
    }

    public static TransportSession getNetworkSession() {
        return new TransportSession(null, Status.NETWORK, null);
    }

    public boolean checkFinishReading() {
        for (var i : receiveFrameList) {
            if (i == null) return false;
        }
        return true;
    }

    public boolean addToList(Frame frame) {
        int pos = (int) frame.getId() - 2;
        if (pos < 0 || pos >= receiveFrameList.length || receiveFrameList[pos] != null) return false;

        receiveFrameList[pos] = frame;
        return true;
    }

    public enum Status {
        NETWORK(),
        IDLE(),
        READING(),
        READINGFINISHED(),
        EXECUTING(),
        SENDING(),
        HALT()
    }
}
