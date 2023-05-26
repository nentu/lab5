package ru.bardinpetr.itmo.lab5.common.io;

import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.common.io.exceptions.FileAccessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Class implementing low level filesystem operations (rw) with required checks.
 */
public class FileController {
    protected final File file;

    /**
     * Initializes work with file, this command must be called at least once.
     * File is existence is ensured. File permissions are checked.
     *
     * @param filePath path to the target file, will be saved and reused
     */
    public FileController(@NonNull String filePath) throws FileAccessException {
        this(filePath, true);
    }

    public FileController(@NonNull String filePath, boolean createIfNotExists) throws FileAccessException {
        file = loadPath(filePath);
        ensureExistence(createIfNotExists);
        checkAccess();
    }

    /**
     * Checks formal path validity and creates io.File for it.
     *
     * @param path File path
     * @return File object if file is valid
     */
    private File loadPath(String path) throws FileAccessException {
        try {
            Paths.get(path);
            return new File(path);
        } catch (InvalidPathException ex) {
            throw new FileAccessException(ex, path, FileAccessException.OperationType.OPEN);
        }
    }

    /**
     * Check if file exists w\o altering FS. See ensureExistence
     *
     * @return if file exists
     */
    private boolean checkExistence() {
        try {
            ensureExistence(false);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Check if file exists (and is a normal file), create if not exists.
     *
     * @param autoCreate true for allowing of creation of non-existent file
     * @return if file was created during this process
     * @throws FileAccessException when file not exist and could not be created
     */
    protected boolean ensureExistence(boolean autoCreate) throws FileAccessException {
        if (file.isFile()) return false;
        if (!autoCreate)
            throw new FileAccessException(
                    file.getAbsolutePath(),
                    FileAccessException.OperationType.OPEN
            );

        try {
            return file.createNewFile();
        } catch (IOException | SecurityException ex) {
            throw new FileAccessException(
                    ex,
                    file,
                    FileAccessException.OperationType.CREATE
            );
        }
    }

    /**
     * Checks if program have write access to file.
     *
     * @throws FileAccessException if file is not readable
     */
    protected void checkWriteAccess() throws FileAccessException {
        if (!file.canWrite())
            throw new FileAccessException(file, FileAccessException.OperationType.PERM_WRITE);
    }

    /**
     * Checks if program have read access to file.
     *
     * @throws FileAccessException if file is not writable
     */
    protected void checkReadAccess() throws FileAccessException {
        if (!file.canRead())
            throw new FileAccessException(file, FileAccessException.OperationType.PERM_READ);
    }

    /**
     * Checks read and write access. See checkReadAccess and checkWriteAccess.
     *
     * @throws FileAccessException if not enough permissions
     */
    private void checkAccess() throws FileAccessException {
        checkReadAccess();
        checkWriteAccess();
    }

    /**
     * Run existence and access checks.
     * Not altering FS at all.
     *
     * @return true if checks ok
     */
    public boolean check() throws FileAccessException {
        try {
            ensureExistence(false);
            checkAccess();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * @return Get file path
     */
    public String getPath() {
        return file.getAbsolutePath();
    }

    public ZonedDateTime creationDate() {
        try {
            var attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return ZonedDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
