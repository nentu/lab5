package ru.bardinpetr.itmo.lab5.client;

public class LocalScriptExecutorTests {
//    Path path = Paths.get("scripts");
//
//    private final String ordPath = path.toAbsolutePath().toString();
//    private final String elementText = """
//            Artem
//            12.3
//            02-03-2023
//            02-03-2023
//            13
//            12
//            C
//            ITMO
//            PUBLIC
//            CLEANER""";
//
//    private void createScript(String path, String text) throws IOException {
//        var file = new FileOutputStream(path);
//        file.write(text.getBytes(StandardCharsets.UTF_8));
//        file.close();
//    }
//
//    private Worker createWorker() {
//        return new Worker(
//                0,
//                ZonedDateTime.of(2023, 10, 10, 12, 12, 12, 12, ZoneId.of("UTC")),
//                "Artem",
//                new Coordinates(13, 12),
//                12.3f,
//                new Date(2023 - 1900, Calendar.MARCH, 2),
//                new Organization("ITMO", OrganizationType.PUBLIC),
//                LocalDate.of(2023, Month.MARCH, 2),
//                Position.CLEANER
//        );
//
//    }
//
//    private void testSingleFile(String text, List<APICommand> result) throws IOException {
//        String fileName = ordPath + "TestScript1.zb";
//        createScript(fileName, text);
//
//        executeScript(fileName, result);
//
//    }
//
//    private void executeScript(String fileName, List<APICommand> result) throws IOException {
//
//        HashMap<String, Object> objectMap = new HashMap<>();
//        objectMap.put("fileName", fileName);
//
////        var payload = (new ObjectMapper()).convertValue(
////                objectMap, LocalExecuteScriptCommand.class);
//
////        ScriptExecutor executor = new ScriptExecutor();
////        var respond = (ExecuteScriptCommand) executor.execute(payload).getPayload();
//
////        assertEquals(result, respond.getCommands());
//    }
//
//    @Test
//    @DisplayName("Testing single file with single command")
//    void singleCommand() {
//        assertDoesNotThrow(() -> {
//            testSingleFile("""
//                            help
//                            """,
//                    new ArrayList<>() {{
//                        add(new HelpCommand());
//                    }}
//            );
//        }, "Single command");
//    }
//
//    @Test
//    @DisplayName("Testing single file with single argument command")
//    void singleArgCommand() {
//        assertDoesNotThrow(() -> {
//            testSingleFile(String.format("""
//                            add
//                            %s
//                            """, elementText),
//                    new ArrayList<>() {{
//                        var t = new AddCommand();
//                        t.setElement(createWorker());
//                        add(t);
//                    }}
//            );
//        }, "Single command with worker");
//    }
//
//    @Test
//    @DisplayName("Testing single file with all no argument command")
//    void allNoArgCommands() {
//        assertDoesNotThrow(() -> {
//            testSingleFile("""
//                            help
//                            info
//                            show
//                            clear
//                            save
//                            exit
//                            print_descending
//                            print_unique_organization
//                            """,
//                    new ArrayList<>() {{
//                        add(new HelpCommand());
//                        add(new InfoCommand());
//                        add(new ShowCommand());
//                        add(new ClearCommand());
//                        add(new SaveCommand());
//                        add(new ExitCommand());
//                        add(new PrintDescendingCommand());
//                        add(new UniqueOrganisationCommand());
//                    }}
//            );
//        });
//    }
//
//    @Test
//    @DisplayName("Testing single file with all argument command")
//    void allArgCommandsFirstPart() {
//        assertDoesNotThrow(() -> {
//            testSingleFile(String.format("""
//                                    clear
//                                    add
//                                    %s
//                                    update %d
//                                    %s
//                                    remove_by_id %d
//                                    add_if_max
//                                    %s
//                                    add_if_min
//                                    %s
//                                    remove_greater
//                                    %s
//                                    filter_less_than_position %s
//                                    """,
//                            elementText,
//                            0, elementText,
//                            0,
//                            elementText,
//                            elementText,
//                            elementText,
//                            "CLEANER"),
//                    new ArrayList<>() {{
//                        add(new ClearCommand());
//
//                        var t1 = new AddCommand();
//                        t1.setElement(createWorker());
//                        add(t1);
//
//                        var t2 = new UpdateCommand();
//                        t2.setId(0);
//                        t2.setElement(createWorker());
//                        add(t2);
//
//                        var t3 = new RemoveByIdCommand();
//                        t3.setId(0);
//                        add(t3);
//
//                        var t4 = new AddIfMaxCommand();
//                        t4.setElement(createWorker());
//                        add(t4);
//
//                        var t5 = new AddIfMinCommand();
//                        t5.setElement(createWorker());
//                        add(t5);
//
//                        var t6 = new RemoveGreaterCommand();
//                        t6.setElement(createWorker());
//                        add(t6);
//
//                        var t7 = new FilterLessPosCommand();
//                        t7.setPosition(Position.CLEANER);
//                        add(t7);
//                    }}
//            );
//        });
//    }
//
//    @Test
//    @DisplayName("Testing two connected scripts")
//    void correctTwoScripts() {
//        assertDoesNotThrow(() -> {
//            createScript(ordPath + "DoubleScript1.zb", String.format("""
//                    clear
//                    execute_script %sDoubleScript2.zb
//                    show
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript2.zb", String.format("""
//                            info
//                            add
//                            %s
//                            """,
//                    elementText));
//            executeScript(ordPath + "DoubleScript1.zb",
//                    new ArrayList<>() {{
//                        add(new ClearCommand());
//
//                        List<APICommand> commands = new ArrayList<>() {{
//                            add(new InfoCommand());
//                            var t = new AddCommand();
//                            t.setElement(createWorker());
//                            add(t);
//                        }};
//
//                        add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(commands));
//
//                        add(new ShowCommand());
//                    }});
//        });
//
//    }
//
//    @Test
//    @DisplayName("Testing recursion limit")
//    void infinityRecursionTwoScripts() {
//        assertThrows(NullPointerException.class, () -> {
//            createScript(ordPath + "DoubleScript1.zb", String.format("""
//                    clear
//                    execute_script %sDoubleScript2.zb
//                    show
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript2.zb", String.format("""
//                    execute_script %sDoubleScript2.zb
//                    """, ordPath));
//            executeScript(ordPath + "DoubleScript1.zb",
//                    new ArrayList<>() {{
//                        add(new ClearCommand());
//
//                        List<APICommand> commands = new ArrayList<>() {{
//                            add(new InfoCommand());
//                            var t = new AddCommand();
//                            t.setElement(createWorker());
//                            add(t);
//                        }};
//
//                        add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(commands));
//
//                        add(new ShowCommand());
//                    }});
//        }, "unlimited recursion is not allowed");
//
//    }
//
//    @Test
//    @DisplayName("Testing recursion depth")
//    void recursionFourDepth() {
//        assertDoesNotThrow(() -> {
//            createScript(ordPath + "DoubleScript1.zb", String.format("""
//                    clear
//                    execute_script %sDoubleScript2.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript2.zb", String.format("""
//                    execute_script %sDoubleScript3.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript3.zb", String.format("""
//                    execute_script %sDoubleScript4.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript4.zb", String.format("""
//                    info
//                    """, ordPath));
//            executeScript(ordPath + "DoubleScript1.zb",
//                    new ArrayList<>() {{
//                        add(new ClearCommand());
//
//                        add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                            add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                                add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                                    add(new InfoCommand());
//                                }}
//                                ));
//                            }}
//                            ));
//                        }}
//                        ));
//                    }}
//            );
//        }, "unlimited recursion is not allowed");
//
//    }
//
//    @Test
//    @DisplayName("Testing recursion depth with error")
//    void recursionFourDepthCommandError() {
//        assertThrows(NullPointerException.class, () -> {
//            createScript(ordPath + "DoubleScript1.zb", String.format("""
//                    clear
//                    execute_script %sDoubleScript2.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript2.zb", String.format("""
//                    execute_script %sDoubleScript3.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript3.zb", String.format("""
//                    execute_script %sDoubleScript4.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript4.zb", String.format("""
//                    infod
//                    """, ordPath));
//            executeScript(ordPath + "DoubleScript1.zb",
//                    new ArrayList<>() {{
//                        add(new ClearCommand());
//
//                        add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                            add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                                add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                                    add(new InfoCommand());
//                                }}
//                                ));
//                            }}
//                            ));
//                        }}
//                        ));
//                    }}
//            );
//        }, "unlimited recursion is not allowed");
//
//    }
//
//    @Test
//    @DisplayName("Testing recursion depth with invalid file name")
//    void recursionFourDepthFileNameError() {
//        assertThrows(NullPointerException.class, () -> {
//            createScript(ordPath + "DoubleScript1.zb", String.format("""
//                    clear
//                    execute_script %sDoubleScript2.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript2.zb", String.format("""
//                    execute_script %sDoubleScript3.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript3.zb", String.format("""
//                    execute_script %sDoubleScrip5t4.zb
//                    """, ordPath));
//            createScript(ordPath + "DoubleScript4.zb", """
//                    info
//                    """);
//            executeScript(ordPath + "DoubleScript1.zb",
//                    new ArrayList<>() {{
//                        add(new ClearCommand());
//
//                        add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                            add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                                add(new LocalExecuteScriptCommand.LocalExecuteScriptCommandResponse(new ArrayList<>() {{
//                                    add(new InfoCommand());
//                                }}
//                                ));
//                            }}
//                            ));
//                        }}
//                        ));
//                    }}
//            );
//        }, "unlimited recursion is not allowed");
//
//    }

}
