package ru.bardinpetr.itmo.lab5.common.ui;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

@Slf4j
public abstract class AbstractConsoleArgumentsParser {
    private final HelpFormatter formatter = new HelpFormatter();
    private final CommandLineParser parser = new DefaultParser();
    private final CommandLine parsedOptions;

    public AbstractConsoleArgumentsParser(String[] args) {
        var currentOptions = createOptions();
        parsedOptions = parse(currentOptions, args);
    }

    public CommandLine getOptions() {
        return parsedOptions;
    }

    private CommandLine parse(Options options, String[] args) {
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Lab5", options);
            System.exit(1);
        }
        return null;
    }

    abstract protected Options createOptions();
}
