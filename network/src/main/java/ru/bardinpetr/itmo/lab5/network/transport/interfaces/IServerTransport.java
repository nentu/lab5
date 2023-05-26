package ru.bardinpetr.itmo.lab5.network.transport.interfaces;


import ru.bardinpetr.itmo.lab5.network.transport.handlers.IMessageHandler;

/**
 * Interface for server transport protocols implementations
 *
 * @param <U> type of low-level user identifier
 * @param <L> base low-level message type
 */
public interface IServerTransport<U, L> extends Runnable {

    /**
     * Synchronously send request.
     */
    void send(U recipient, L data);

    /**
     * Subscribe on messages arriving
     *
     * @param handler message handler
     */
    void subscribe(IMessageHandler<U, L> handler);

    /**
     * Start main reception loop
     */

}
