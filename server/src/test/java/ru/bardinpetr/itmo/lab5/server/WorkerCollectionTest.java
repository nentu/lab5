//package ru.bardinpetr.itmo.lab5.server;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import ru.bardinpetr.itmo.lab5.common.io.FileIOController;
//import ru.bardinpetr.itmo.lab5.common.io.exceptions.FileAccessException;
//import ru.bardinpetr.itmo.lab5.models.commands.AddCommand;
//import ru.bardinpetr.itmo.lab5.models.commands.AddIfMaxCommand;
//import ru.bardinpetr.itmo.lab5.models.commands.ClearCommand;
//import ru.bardinpetr.itmo.lab5.models.data.Worker;
//import ru.bardinpetr.itmo.lab5.models.data.collection.WorkerCollection;
//import ru.bardinpetr.itmo.lab5.server.filedb.FileDBController;
//import ru.bardinpetr.itmo.lab5.server.utils.WorkerFactory;
//
//import java.awt.event.TextEvent;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class WorkerCollectionTest {
//
//    @Test
//    @DisplayName("CRUD operations tests")
//    void CRUDTests() {
//        var file = FileUtils.createFile(FileUtils.createDir("rwx"), "rw-");
//        FileIOController ioController;
//        try {
//            ioController = new FileIOController(file.getPath());
//        } catch (Exception e) {
//            fail("should setup FileDB");
//            return;
//        }
//
//        var exec = new MainExecutor(ioController);
//        var data = exec.getDb().data();
//
//        var worker1 = WorkerFactory.create();
//
//        var res = exec.execute(new AddCommand(worker1));
//        assertTrue(res.isSuccess());
//        assertTrue(data.contains(worker1));
//
//
////         Add data directly to the db
//        var data = db.data();
//        data.add(new Worker());
//    }
//}
