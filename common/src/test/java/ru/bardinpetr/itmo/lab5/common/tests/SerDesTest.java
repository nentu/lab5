//package ru.bardinpetr.itmo.lab5.common.tests;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import ru.bardinpetr.itmo.lab5.common.serdes.XMLSerDesService;
//import ru.bardinpetr.itmo.lab5.models.data.*;
//
//import java.time.LocalDate;
//import java.time.ZonedDateTime;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class SerDesTest {
//
//    @Test
//    @DisplayName("Deserialization of serialized Worker object should succeed and return the same object")
//    void workerSerializationTest() {
//        var worker = new Worker(
//                1,
//                ZonedDateTime.now(),
//                "test_name",
//                44.55f,
//                new Date(),
//                LocalDate.now(),
//                new Coordinates(11, 22.33f),
//                new Organization("test_org", OrganizationType.COMMERCIAL),
//                Position.CLEANER
//        );
//
//        var service = new XMLSerDesService<>(Worker.class);
//        assertDoesNotThrow(() -> {
//            var serialized = service.serialize(worker);
//            var deserialized = service.deserialize(serialized);
//
//            assertEquals(worker,
//                    deserialized.withCreationDate(
//                            deserialized
//                                    .getCreationDate()
//                                    .withZoneSameLocal(worker.getCreationDate().getZone())
//                    )
//            );
//        });
//    }
//}
