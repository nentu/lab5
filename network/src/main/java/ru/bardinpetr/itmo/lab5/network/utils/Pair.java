package ru.bardinpetr.itmo.lab5.network.utils;

import lombok.Data;

import java.io.IOException;

/**
 * @return pair of sender socket address and received data
 * @throws IOException
 */
@Data
public class Pair<T, K> {
    private final T first;
    private final K second;
}