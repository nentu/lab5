package ru.bardinpetr.itmo.lab5.common.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.bardinpetr.itmo.lab5.common.io.FileIOController;
import ru.bardinpetr.itmo.lab5.common.io.exceptions.FileAccessException;
import ru.bardinpetr.itmo.lab5.common.tests.utils.FileUtils;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileControllerTests {

    @Test
    @DisplayName("Initialization test")
    void createFileTest() {
        var dir = FileUtils.createDir("rwx");
        var file = FileUtils.createFile(dir, "rw-");
        var path = file.getPath();

        // No file test
        assertDoesNotThrow(() -> {
            new FileIOController(path);
            assertTrue(FileUtils.exists(file), "File should be created or exist");
        }, "In normal environment initialization without file should succeed");

        // Existing file test
        var test_data = "test";
        FileUtils.write(file, test_data);
        assertDoesNotThrow(() -> {
                    var controller = new FileIOController(path);

                    assertTrue(FileUtils.exists(file), "File should still exist after initialization");
                    assertEquals(test_data, FileUtils.read(file), "Existing file should not be modified");

                    assertTrue(controller.check(), "After initialization without errors checks should succeed");
                },
                "In normal environment initialization with existing file should succeed"
        );

        FileUtils.deleteDir(dir);
    }

    @Test
    @DisplayName("Insufficient permissions test")
    void permissionsTest() {
        Path dir = FileUtils.createDir("r-x");
        String path = dir.resolve("test.dat").toString();

        try {
            new FileIOController(path);
        } catch (FileAccessException e) {
            assertEquals(
                    e.getType(), FileAccessException.OperationType.CREATE,
                    "Should throw exception if directory is not writable");
        }

        FileUtils.deleteDir(dir);

        // Initial file permissions
        dir = FileUtils.createDir("rwx");

        for (String perms : List.of("-w-", "r--")) {
            try {
                new FileIOController(
                        FileUtils.createFile(dir, perms)
                                .getPath()
                );
            } catch (FileAccessException e) {
                assertEquals(
                        e.getType(),
                        perms.contains("r") ? FileAccessException.OperationType.PERM_WRITE : FileAccessException.OperationType.PERM_READ,
                        "Should throw exception if file has %s permissions".formatted(perms));
            }
        }

        // Normal creation then broken
        var file = FileUtils.createFile(dir, "rw-");
        assertDoesNotThrow(() -> {
            var controller = new FileIOController(file.getPath());

            file.setReadable(false);

            try {
                controller.read();
            } catch (FileAccessException e) {
                assertEquals(
                        e.getType(),
                        FileAccessException.OperationType.PERM_READ,
                        "After successful creation should throw exception if file is tried to be read but w\\o R permission");
            }

            file.setReadable(true);
            file.setWritable(false);
            try {
                controller.write("test".getBytes());
            } catch (FileAccessException e) {
                assertEquals(
                        e.getType(),
                        FileAccessException.OperationType.PERM_WRITE,
                        "After successful creation should throw exception if file is tried to be written but w\\o W permission");
            }
        });

        FileUtils.deleteDir(dir);
    }

    @Test
    @DisplayName("Runtime file altering after creation")
    void existenceTest() {
        var dir = FileUtils.createDir("rwx");
        var file = FileUtils.createFile(dir, "rw-");

        FileIOController controller;
        try {
            controller = new FileIOController(file.getPath());
        } catch (FileAccessException e) {
            fail("Controller should successfully initialize in normal environment");
            return;
        }

        var test_data = "test";
        assertDoesNotThrow(() -> controller.write(test_data.getBytes()));
        assertEquals(test_data, FileUtils.read(file), "File should be written successfully");

        file.delete();
        assertFalse(file.exists());

        try {
            controller.read();
        } catch (FileAccessException ex) {
            assertEquals(
                    FileAccessException.OperationType.OPEN,
                    ex.getType(),
                    "After deletion file read should throw specific exception");
            assertFalse(file.isFile(), "File should not be created by read procedure");
        }

        assertDoesNotThrow(() -> controller.write(test_data.getBytes()), "Write to file that has been deleted should succeed with creation of new file");
        assertTrue(FileUtils.exists(file), "New file should be created");
        assertEquals(test_data, FileUtils.read(file), "New file writen should have valid content");

        FileUtils.deleteDir(dir);
    }

    @Test
    @DisplayName("Read/Write file access")
    void rwTest() {
        var dir = FileUtils.createDir("rwx");
        var file = FileUtils.createFile(dir, "rw-");

        FileIOController controller;
        try {
            controller = new FileIOController(file.getPath());
        } catch (FileAccessException e) {
            fail("Controller should successfully initialize in normal environment");
            return;
        }

        var test_data = "test";
        assertDoesNotThrow(() -> controller.write(test_data.getBytes()), "File should be written successfully");
        assertEquals(test_data, FileUtils.read(file), "File contents after write should match with original");

        assertDoesNotThrow(
                () -> assertArrayEquals(
                        test_data.getBytes(), controller.read(),
                        "File contents after read should match with original"
                ),
                "File should be written successfully"
        );

        FileUtils.deleteDir(dir);
    }
}
