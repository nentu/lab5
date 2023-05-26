package ru.bardinpetr.itmo.lab5.server.app.ui;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.bardinpetr.itmo.lab5.common.ui.AbstractConsoleArgumentsParser;

public class ServerConsoleArgumentsParser extends AbstractConsoleArgumentsParser {
    public ServerConsoleArgumentsParser(String[] args) {
        super(args);
    }

    @Override
    protected Options createOptions() {
        var options = new Options();
        options.addOption(
                Option.builder()
                        .option("b")
                        .longOpt("bootstrap")
                        .desc("clear database and recreate tables")
                        .build()
        );


        options.addOption(
                Option.builder()
                        .option("d")
                        .longOpt("dburl")
                        .hasArg(true)
                        .desc("database connection url")
                        .build()
        );


        var dbuser = Option.builder()
                .option("u")
                .longOpt("dbuser")
                .hasArg(true)
                .desc("database username")
                .build();
        dbuser.setRequired(true);
        options.addOption(dbuser);


        var dbpass = Option.builder()
                .option("p")
                .longOpt("dbpass")
                .hasArg(true)
                .desc("database password")
                .build();
        dbpass.setRequired(true);
        options.addOption(dbpass);


        var port = Option.builder()
                .longOpt("port")
                .hasArg(true)
                .desc("UDP port")
                .build();
        port.setRequired(true);
        options.addOption(port);

        return options;
    }

    public String getDatabaseUrl() {
        return getOptions().getOptionValue("dburl", "jdbc:postgresql://localhost:5432/studs");
    }

    public String getUsername() {
        return getOptions().getOptionValue("dbuser");
    }

    public String getPassword() {
        return getOptions().getOptionValue("dbpass");
    }

    public Integer getPort() {
        try {
            return Integer.parseInt(getOptions().getOptionValue("port", "5000"));
        } catch (NumberFormatException ignored) {
            System.err.println("invalid port");
            System.exit(1);
        }
        return null;
    }

    public Boolean doBootstrap() {
        return getOptions().hasOption("bootstrap");
    }
}
