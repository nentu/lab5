package ru.bardinpetr.itmo.lab5.common.io;

import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.common.io.exceptions.FileAccessException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Controller for streaming IO operations on file.
 */
public class FileIOController extends FileController {
    /**
     * Initializes work with file, this command must be called at least once.
     * File is existence is ensured. File permissions are checked.
     *
     * @param filePath path to the target file, will be saved and reused
     */
    public FileIOController(@NonNull String filePath) throws FileAccessException {
        this(filePath, true);
    }

    public FileIOController(@NonNull String filePath, boolean createIfNotExists) throws FileAccessException {
        super(filePath, createIfNotExists);
    }

    /**
     * Create file read stream.
     * Checks existence (non-altering) and R permission.
     *
     * @return opened FileInputStream
     * @throws FileAccessException thrown if file not exists or not enough permissions
     */
    public FileInputStream openReadStream() throws FileAccessException {
        ensureExistence(false);
        checkReadAccess();
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException | SecurityException ex) {
            throw new FileAccessException(ex, file, FileAccessException.OperationType.PERM_READ);
        }
    }


    /**
     * Create file write stream.
     * Checks existence (creates if not) and W permission.
     *
     * @return opened FileOutputStream
     * @throws FileAccessException thrown if file not exists or not enough permissions
     */
    public FileOutputStream openWriteStream() throws FileAccessException {
        ensureExistence(true);
        checkWriteAccess();
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException | SecurityException ex) {
            throw new FileAccessException(ex, file, FileAccessException.OperationType.PERM_WRITE);
        }
    }

    /**
     * Write bytes to file
     *
     * @param data bytes to write
     * @throws FileAccessException thrown with corresponding OperationType for permission or IO error
     */
    public void write(byte[] data) throws FileAccessException {
        try (var stream = openWriteStream()) {
            stream.write(data);
        } catch (IOException ex) {
            throw new FileAccessException(file, FileAccessException.OperationType.WRITE);
        }
    }

    /**
     * Read full file as bytes
     *
     * @return file contents
     * @throws FileAccessException thrown with corresponding OperationType for permission or IO error
     */
    public byte[] read() throws FileAccessException {
        try (var stream = openReadStream()) {
            return stream.readAllBytes();
        } catch (IOException ex) {
            throw new FileAccessException(file, FileAccessException.OperationType.READ);
        }
    }

    /**
     * Clears file content. If file not exist creates empty
     */
    public void clear() throws FileAccessException {
        ensureExistence(true);
        write(new byte[]{});
    }
}
