package ru.bardinpetr.itmo.lab5.common.tests.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Comparator;
import java.util.Set;

public class FileUtils {
    /**
     * Creates FileAttribute for posix-style permission string for user only, group and others set to 'r'.
     *
     * @param posixPermissions 'rwx' for user permissions, if permission is not set put '-' instead of letter, e.g. -w- for write only
     * @return FileAttribute built from permissions
     */
    public static FileAttribute<Set<PosixFilePermission>> parsePermissions(String posixPermissions) {
        var full = "%sr--r--".formatted(posixPermissions);
        return PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(full));
    }

    /**
     * Create temporary directory
     *
     * @param posixPermissions @see FileUtils.parsePermissions
     */
    public static Path createDir(String posixPermissions) {
        var perms = parsePermissions(posixPermissions);
        try {
            return Files.createTempDirectory("dbfolder", perms);
        } catch (IOException e) {
            throw new RuntimeException("Could not create temp file");
        }
    }

    /**
     * Create temporary file in directory which will be automatically deleted
     *
     * @param filePermissions @see FileUtils.parsePermissions
     */
    public static File createFile(Path dir, String filePermissions) {
        var perms = parsePermissions(filePermissions);
        try {
            var file = Files.createTempFile(dir, "db", ".tmp", perms).toFile();
            file.deleteOnExit();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(File file, String data) {
        try (var st = new FileOutputStream(file)) {
            st.write(data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Can't create file");
        }
    }

    public static String read(File file) {
        byte[] res;
        try (var st = new FileInputStream(file)) {
            res = st.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Can't create file");
        }
        return new String(res);
    }

    public static void deleteDir(Path path) {
        try (var files = Files.walk(path)) {
            files
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            path.toFile().delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(File file) {
        file.delete();
    }

    public static boolean exists(File file) {
        return file.isFile();
    }

    public static File getParentDir(File file) {
        return file.getAbsoluteFile().getParentFile();
    }

    public static boolean checkParentDirectoryPermissions(File file) {
        var dir = getParentDir(file);
        return dir.canWrite() && dir.canRead() && dir.canExecute();
    }
}
