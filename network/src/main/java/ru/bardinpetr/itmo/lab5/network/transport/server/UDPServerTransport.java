package ru.bardinpetr.itmo.lab5.network.transport.server;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.serdes.JSONSerDesService;
import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;
import ru.bardinpetr.itmo.lab5.network.transport.handlers.IMessageHandler;
import ru.bardinpetr.itmo.lab5.network.transport.interfaces.IServerTransport;
import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;
import ru.bardinpetr.itmo.lab5.network.transport.models.SocketMessage;
import ru.bardinpetr.itmo.lab5.network.transport.models.TransportSession;
import ru.bardinpetr.itmo.lab5.network.utils.Pair;
import ru.bardinpetr.itmo.lab5.network.utils.TransportUtils;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

/**
 * Server class for receiving and sending messages from clients
 */
@Slf4j
@Deprecated
public class UDPServerTransport implements IServerTransport<SocketAddress, SocketMessage> {
    private static final int TIMEOUT = 1000;
    private final Selector selector = Selector.open();
    private final Queue<Pair<SocketAddress, Frame>> sendFrameQueue = new ArrayDeque<>();
    private final SelectionKey networkKey;
    private final DatagramChannel datagramChannel;
    private final Map<SocketAddress, Pipe.SinkChannel> clientSinkMap = new HashMap<>();
    JSONSerDesService<SocketMessage> serDesService = new JSONSerDesService<>(SocketMessage.class);
    private IMessageHandler<SocketAddress, SocketMessage> handler = null;


    public UDPServerTransport(DatagramChannel channel) throws IOException {
        datagramChannel = channel;
        networkKey = channel.register(selector, SelectionKey.OP_READ, TransportSession.getNetworkSession());
    }

    /**
     * Main loop for receiving client from server and executing
     */
    @Override
    public void run() {
        log.info("listening..");

        while (true) {
            // Wait for task or until timeout expires
            try {
                if (selector.select(TIMEOUT) == 0) continue;

                // Get iterator on set of keys with I/O to process
                var keyIter = selector.selectedKeys().iterator();
                while (keyIter.hasNext()) {
                    SelectionKey key = keyIter.next(); // Key is bit mask
                    keyIter.remove();

                    // Client socket channel has pending data?
                    if (key.isReadable()) {
                        if (key.equals(networkKey)) { //Data channel message
                            DatagramChannel channel = (DatagramChannel) key.channel();

                            ByteBuffer buffer = ByteBuffer.allocate(Frame.MAX_SIZE);
                            SocketAddress address = channel.receive(buffer);

                            Frame frame = Frame.fromBytes(buffer.array());

                            if (clientSinkMap.containsKey(address)) {
                                var sink = clientSinkMap.get(address);
                                sink.write(ByteBuffer.wrap(frame.toBytes()));
                            } else if (frame.getId() == Frame.FIRST_ID) {
                                regRecvClient(address, frame);
                            }

                        } else { //internal message
                            TransportSession session = (TransportSession) key.attachment();

                            Frame frame = Frame.fromChannel((Pipe.SourceChannel) key.channel());

                            if (session.getStatus().equals(TransportSession.Status.IDLE)) {
                                headerFrame(session, frame);
                            } else if (session.getStatus().equals(TransportSession.Status.READING)) {
                                readNewFrame(key, frame);
                            } else if (session.getStatus().equals(TransportSession.Status.SENDING)) {
                                if (session.getSendFrameList().size() > 1) {
                                    scheduleSend(
                                            new Pair<>(
                                                    session.getConsumerAddress(),
                                                    session.getSendFrameList().remove(0)
                                            )
                                    );
                                } else {
                                    log.debug("Sending finished");
                                    session.setStatus(TransportSession.Status.HALT);
                                    closeSessionByKey(key);

                                    scheduleSend(
                                            new Pair<>(
                                                    session.getConsumerAddress(),
                                                    session.getSendFrameList().remove(0)
                                            )
                                    );
                                }

                            }

                        }
                    }

                }

                for (int i = 0; i < sendFrameQueue.size(); i++) {
                    var pair = sendFrameQueue.remove();
                    datagramChannel.send(
                            ByteBuffer.wrap(pair.getSecond().toBytes()),
                            pair.getFirst());
                }
            } catch (IOException ignored) {
                continue;
            }
        }
    }

    private void scheduleSend(Pair<SocketAddress, Frame> pair) {
        var frame = pair.getSecond();
        var address = pair.getFirst();

        try {
            datagramChannel.send(
                    ByteBuffer.wrap(frame.toBytes()),
                    address
            );
        } catch (IOException ignored) {
        }
        log.debug("send frame %d".formatted(frame.getId()));
//        sendFrameQueue.add(pair);
    }

    /**
     * Adding frame to receiving list, adding ACK to sending list, check if finished
     *
     * @param key   Client key
     * @param frame
     */
    private void readNewFrame(SelectionKey key, Frame frame) {
        log.debug("read frame %d".formatted(frame.getId()));
        TransportSession session = (TransportSession) key.attachment();
        session.addToList(frame);

        if (session.checkFinishReading()) {
            closeSessionByKey(key);

            scheduleSend(
                    new Pair<>(
                            session.getConsumerAddress(),
                            new Frame(frame.getId())
                    )
            );

            finishReading((TransportSession) key.attachment());
        } else {
            scheduleSend(
                    new Pair<>(
                            session.getConsumerAddress(),
                            new Frame(frame.getId())
                    )
            );
        }


    }

    /**
     * Serialize socket message form received byte list
     *
     * @param session client session
     */
    private void finishReading(TransportSession session) {
        session.setStatus(TransportSession.Status.READINGFINISHED);

        var desBytes = TransportUtils.joinFrames(List.of(session.getReceiveFrameList()));
        SocketMessage msg;
        try {
            msg = serDesService.deserialize(desBytes);
        } catch (SerDesException e) {
            msg = new SocketMessage(new byte[]{});
        }
        var address = session.getConsumerAddress();

        handler.handle(
                address,
                msg
        );
        log.debug("Finish reading");
    }

    /**
     * Receive header frame with frame count
     *
     * @param session current user session
     * @param frame   received header frame
     */
    private void headerFrame(TransportSession session, Frame frame) {
        log.debug("Start reading");
        int framesCount;
        try {
            framesCount = ByteBuffer.wrap(frame.getPayload()).getInt();
        } catch (BufferUnderflowException ignored) {
            scheduleSend(
                    new Pair<>(
                            session.getConsumerAddress(),
                            new Frame(Frame.INVALID_ID)
                    )
            );
            return;
        }
        scheduleSend(
                new Pair<>(
                        session.getConsumerAddress(),
                        new Frame(Frame.FIRST_ID)
                )
        );
        session.setReceiveFrameList(new Frame[framesCount]);
        session.setStatus(TransportSession.Status.READING);
    }

    /**
     * Registration new clint session with IDLE status
     *
     * @param address
     * @param frame
     * @throws IOException
     */
    private void regRecvClient(SocketAddress address, Frame frame) throws IOException {
        regClient(address, frame, TransportSession.Status.IDLE, null);
    }

    /**
     * Registration new client session with SENDING status
     *
     * @param address   client address
     * @param frame     header frame
     * @param frameList list for sending
     * @throws IOException
     */
    private void regSendClient(SocketAddress address, Frame frame, List<Frame> frameList) throws IOException {
        regClient(address, frame, TransportSession.Status.SENDING, frameList);
    }

    /**
     * @param address  client address
     * @param frame    header frame
     * @param status   status
     * @param sendList frame list for sending
     * @throws IOException
     */
    private void regClient(SocketAddress address, Frame frame, TransportSession.Status status, List<Frame> sendList) throws IOException {
        log.info("Reg new client: " + address);
        var pipe = Pipe.open();

        var sink = pipe.sink();
        var source = pipe.source();


        source.configureBlocking(false);
        source.register(
                selector,
                SelectionKey.OP_READ,
                new TransportSession(
                        address,
                        status,
                        pipe,
                        sendList
                ));
        clientSinkMap.put(
                address,
                sink
        );
        if (sendList == null) {
            sink.write(ByteBuffer.wrap(frame.toBytes()));
        }

    }

    /**
     * Close sink and source channels, remove client from clientSinkMap and close this selector channel by key
     *
     * @param key selector key to be closed
     */
    private void closeSessionByKey(SelectionKey key) {
        var session = (TransportSession) key.attachment();
        try {
            var pipe = session.getPipe();
            pipe.sink().close();
            pipe.source().close();
            clientSinkMap.remove(session.getConsumerAddress());
            key.cancel();
        } catch (IOException e) {
            log.error("Session finish error: " + e.getMessage());
        }
        session = null;
    }

    /**
     * Register client session with frame list for sending and put it to the sending queue
     *
     * @param recipient
     * @param data
     */
    @Override
    public void send(SocketAddress recipient, SocketMessage data) {
        List<Frame> frameList = null;

        try {
            frameList = TransportUtils.separateBytes(
                    serDesService.serialize(data)
            );
        } catch (SerDesException ignored) {
        }

        try {
            regSendClient(
                    recipient,
                    new Frame(
                            Frame.FIRST_ID,
                            TransportUtils.IntToBytes(frameList.size()).array()
                    ),
                    frameList
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scheduleSend(
                new Pair<>(
                        recipient,
                        new Frame(
                                Frame.FIRST_ID,
                                TransportUtils.IntToBytes(frameList.size()).array()
                        )
                )
        );
        log.debug("Start sending message");
    }

    /**
     * Register handler for transporting socket message
     *
     * @param handler message handler
     */
    @Override
    public void subscribe(IMessageHandler<SocketAddress, SocketMessage> handler) {
        this.handler = handler;
    }
}
