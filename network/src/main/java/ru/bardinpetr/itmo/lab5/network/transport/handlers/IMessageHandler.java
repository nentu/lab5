package ru.bardinpetr.itmo.lab5.network.transport.handlers;

/**
 * Function for handling low-level messages
 *
 * @param <U> type of object, that uniquely identifies client before message is parsed
 * @param <M> message type
 */
public interface IMessageHandler<U, M> {
    /**
     * Called when new incoming message arrived from protocol
     *
     * @param sender  low-level user identifier
     * @param message low-level message
     */
    void handle(U sender, M message);
}
