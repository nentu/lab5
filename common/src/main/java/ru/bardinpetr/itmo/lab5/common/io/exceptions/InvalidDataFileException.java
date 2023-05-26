package ru.bardinpetr.itmo.lab5.common.io.exceptions;

import ru.bardinpetr.itmo.lab5.common.serdes.exceptions.SerDesException;

/**
 * Thrown if DB controller tried to load corrupted or empty file
 */
public class InvalidDataFileException extends Exception {
    private final String path;
    private final byte[] contents;

    /**
     * @param path              DB file path
     * @param contents          Full contents of invalid file
     * @param originalException Exception thrown by deserializer
     */
    public InvalidDataFileException(String path, byte[] contents, SerDesException originalException) {
        super(originalException);
        this.path = path;
        this.contents = contents;
    }

    public String getPath() {
        return path;
    }

    public byte[] getContents() {
        return contents;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
