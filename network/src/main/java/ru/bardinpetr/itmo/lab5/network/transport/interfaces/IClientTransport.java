package ru.bardinpetr.itmo.lab5.network.transport.interfaces;


import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportException;
import ru.bardinpetr.itmo.lab5.network.transport.errors.TransportTimeoutException;

import java.time.Duration;

/**
 * Interface for client transport protocols implementations
 *
 * @param <T> base message
 */
public interface IClientTransport<T> {

    /**
     * Synchronously send request.
     *
     * @throws TransportTimeoutException if no message arrived before timeout
     * @throws TransportException        if any IO exception occurred
     */
    void send(T data) throws TransportException, TransportTimeoutException;

    /**
     * Synchronously wait for incoming message
     *
     * @param timeout timeout or null if no timeout should be applied
     * @return message or TimeoutException is thrown
     * @throws TransportTimeoutException if no message arrived before timeout
     * @throws TransportException        if any IO exception occurred
     */
    T receive(Duration timeout) throws TransportException, TransportTimeoutException;
}
