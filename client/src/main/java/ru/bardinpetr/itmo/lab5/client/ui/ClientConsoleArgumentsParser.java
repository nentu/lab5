package ru.bardinpetr.itmo.lab5.client.ui;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.bardinpetr.itmo.lab5.common.ui.AbstractConsoleArgumentsParser;

import java.net.InetSocketAddress;

public class ClientConsoleArgumentsParser extends AbstractConsoleArgumentsParser {
    public ClientConsoleArgumentsParser(String[] args) {
        super(args);
    }

    @Override
    protected Options createOptions() {
        var options = new Options();
        Option input = new Option("h", "host", true, "server host address");
        options.addOption(input);

        Option output = new Option("p", "port", true, "server port");
        options.addOption(output);
        return options;
    }

    public String getHost() {
        return getOptions().getOptionValue("host", "localhost");
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

    public InetSocketAddress getServerFullAddr() {
        try {
            return new InetSocketAddress(getHost(), getPort());
        } catch (IllegalArgumentException ignored) {
            System.err.println("Invalid port");
            System.exit(1);
        }
        return null;
    }
}
