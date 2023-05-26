package ru.bardinpetr.itmo.lab5.network.utils;


import ru.bardinpetr.itmo.lab5.network.transport.models.Frame;

import java.io.IOException;

public class UtilsTest {
    public static void main(String[] args) throws IOException {

        var frame = new Frame(1, "asdasd".getBytes());
        System.out.println(Frame.fromBytes(frame.toBytes()).equals(frame));

        var string = "adasdadasdadasdadasdadasdadasd".repeat(10000);
        System.out.println(new String(TransportUtils.joinFrames(TransportUtils.separateBytes(string.getBytes()))).equals(string));
    }
}
