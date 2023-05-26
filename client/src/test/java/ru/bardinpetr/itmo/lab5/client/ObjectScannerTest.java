package ru.bardinpetr.itmo.lab5.client;

/**
 * Test for ObjectScanner class which check scanning Worker, Coordinates, Organization classes with fields validation
 */
public class ObjectScannerTest {

//    private ObjectScanner getScanner(String string) {
//        Scanner scanner = new Scanner(string);
//        return new ObjectScanner(new ConsolePrinter(),
//                scanner);
//
//    }
//
//    /**
//     * Tests for coordinate scanner which check correct data, invalid data, null data
//     */
//    @DisplayName("Coordinates scanner")
//    @Test
//    void coordinatesScanTest() {
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    new Coordinates(100, 100),
//                    getScanner("""
//                            100
//                            100""").scan(Coordinates.class),
//                    "Correct data"
//            );
//        });
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    773
//                    100""").scan(Coordinates.class);
//        }, "X coordinate can't be greater than 773 (1 test)");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    1000
//                    100""").scan(Coordinates.class);
//        }, "X coordinate can't be greater than 773 (2 test)");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    100
//                    -1000""").scan(Coordinates.class);
//        }, "Y coordinate can't be less than -413 (1 test)");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    100
//                    -413""").scan(Coordinates.class);
//        }, "Y coordinate can't be less than -413 (1 test)");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    adadaf
//                    -100""").scan(Coordinates.class);
//        }, "X coordinate must be integer");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    100
//                    sfs""").scan(Coordinates.class);
//        }, "Y coordinate must be integer");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//
//                    100""").scan(Coordinates.class);
//        }, "X can't be null");
//
//        assertThrows(ParserException.class, () -> {
//            getScanner("""
//                    100
//
//                    """).scan(Coordinates.class);
//        }, "Y can't be null");
//
//    }
//
//    /**
//     * Test for Organization scanner which check correct data, invalid data, null data
//     */
//    @DisplayName("Organization scanner")
//    @Test
//    void organizationScanTest() {
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    new Organization("ITMO", OrganizationType.PUBLIC),
//                    getScanner("""
//                            ITMO
//                            PUBLIC""").scan(Organization.class),
//                    "Correct data"
//            );
//        });
//        assertThrows(ParserException.class, () ->
//        {
//            getScanner("""
//
//                    PUBLIC""").scan(Organization.class);
//        }, "Name can't be null");
//
//        assertThrows(ParserException.class, () ->
//        {
//            getScanner("""
//                    ITMO
//                    PUBLI2C""").scan(Organization.class);
//        }, "Type should be from enum list");
//
//        assertThrows(ParserException.class, () ->
//        {
//            getScanner("""
//                    ITMO
//                    """).scan(Organization.class);
//        }, "Type should not be null");
//    }
//
//    /**
//     * Tests for Worker scanner which check only worker's field with correct data, invalid data, null, data
//     */
//    @DisplayName("Worker scanner")
//    @Test
//    void workerScannerTest() {
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    getScanner("""
//                            Artem
//                            12.3
//                            02-03-2023
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class),
//                    new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "Artem",
//                            new Coordinates(13, 12),
//                            12.3f,
//                            new Date(2023 - 1900, Calendar.MARCH, 2),
//                            new Organization("ITMO", OrganizationType.PUBLIC),
//                            LocalDate.of(2023, Month.MARCH, 2),
//                            Position.CLEANER
//                    ),
//                    "Correct data"
//            );
//        });
//
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    getScanner("""
//                            Artem
//                            12.3
//                            02-03-2023
//
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class),
//                    new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "Artem",
//                            new Coordinates(13, 12),
//                            12.3f,
//                            new Date(2023 - 1900, Calendar.MARCH, 2),
//                            new Organization("ITMO", OrganizationType.PUBLIC),
//                            null,
//                            Position.CLEANER
//                    ),
//                    "Correct data with null endDate"
//            );
//        });
//
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    getScanner("""
//                            Artem
//                            12.3
//                            02-03-2023
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//
//                            """).scan(Worker.class),
//                    new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "Artem",
//                            new Coordinates(13, 12),
//                            12.3f,
//                            new Date(2023 - 1900, Calendar.MARCH, 2),
//                            new Organization("ITMO", OrganizationType.PUBLIC),
//                            LocalDate.of(2023, Month.MARCH, 2),
//                            null
//                    ),
//                    "Correct data with null Position"
//            );
//        });
//
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    getScanner("""
//                            Artem
//                            12.3
//                            02-03-2023
//
//                            13
//                            12
//                            N
//                            CLEANER""").scan(Worker.class),
//                    new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "Artem",
//                            new Coordinates(13, 12),
//                            12.3f,
//                            new Date(2023 - 1900, Calendar.MARCH, 2),
//                            null,
//                            null,
//                            Position.CLEANER
//                    ),
//                    "Correct data with null Organization"
//            );
//        });
//
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    getScanner("""
//                            Artem
//                            12.3
//                            02-03-2023
//                            02-03-2023
//                            13
//                            12
//                            N
//
//                            """).scan(Worker.class),
//                    new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "Artem",
//                            new Coordinates(13, 12),
//                            12.3f,
//                            new Date(2023 - 1900, Calendar.MARCH, 2),
//                            null,
//                            LocalDate.of(2023, Month.MARCH, 2),
//                            null
//                    ),
//                    "Correct data with null Organization and Position"
//            );
//        });
//
//        assertDoesNotThrow(() -> {
//            assertEquals(
//                    getScanner("""
//                            Artem
//                            12.3
//                            02-03-2023
//
//                            13
//                            12
//                            N
//
//                            """).scan(Worker.class),
//                    new Worker(
//                            0,
//                            ZonedDateTime.now(),
//                            "Artem",
//                            new Coordinates(13, 12),
//                            12.3f,
//                            new Date(2023 - 1900, Calendar.MARCH, 2),
//                            null,
//                            null,
//                            null
//                    ),
//                    "Correct data with null Organization and Position and endDate"
//            );
//        });
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//
//                            12.3
//                            02-03-2023
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "Name can not be null");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            12ds
//                            02-03-2023
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "Salary must be float");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            123
//
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "Start date can not be null");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            123
//                            44-11-2121
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "Start date should be in format");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            123
//                            24-41-2121
//                            02-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "Start date should be in format");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            123
//                            14-11-2121
//                            32-03-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "End date should be in format");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            123
//                            14-11-2121
//                            12-33-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEANER""").scan(Worker.class);
//                },
//                "End date should be in format");
//
//        assertThrows(ParserException.class,
//                () -> {
//                    getScanner("""
//                            Artem
//                            123
//                            14-11-2121
//                            12-33-2023
//                            13
//                            12
//                            C
//                            ITMO
//                            PUBLIC
//                            CLEA2NER""").scan(Worker.class);
//                },
//                "Position should be from enum list");
//
//
//    }

}
