package ru.bardinpetr.itmo.lab5.mainclient.local.controller.commands;

import ru.bardinpetr.itmo.lab5.client.api.APIClientConnector;
import ru.bardinpetr.itmo.lab5.client.api.commands.APICommandRegistry;
import ru.bardinpetr.itmo.lab5.client.controller.common.APIUILocalCommand;
import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;
import ru.bardinpetr.itmo.lab5.client.ui.cli.utils.ConsolePrinter;
import ru.bardinpetr.itmo.lab5.client.ui.interfaces.UIReceiver;
import ru.bardinpetr.itmo.lab5.common.error.APIClientException;
import ru.bardinpetr.itmo.lab5.models.commands.requests.PagingAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.requests.UserAPICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;
import ru.bardinpetr.itmo.lab5.models.commands.responses.UserPrintableAPICommandResponse;

import java.util.List;
import java.util.Scanner;

/**
 * Worker update command
 */
public abstract class PagingLocalCommand extends APIUILocalCommand {
    private static final int count = 5;

    public PagingLocalCommand(APIClientConnector api, UIReceiver ui, APICommandRegistry registry) {
        super(api, ui, registry);
    }

    @Override
    public String getExternalName() {
        return "null";
    }

    @Override
    protected UserAPICommand retrieveAPICommand(String name) {
        return null;
    }

    protected abstract PagingAPICommand createPagedCommand(int offset, int count);


    @Override
    public ClientCommandResponse<? extends UserPrintableAPICommandResponse> executeWithArgs(List<String> args) {
        var printer = new ConsolePrinter();
        Scanner scanner = new Scanner(System.in);

        int offset = -count;
        String input = "U";
        boolean isOut = false;
        int prevOffset = 0;

        while (true) {
            APICommandResponse resp;
            prevOffset = offset;
            try {

                if (input.equals("U")) {
                    offset += count;
                } else if (input.equals("D")) {
                    if (offset <= 0) {
                        printer.display("Encountered start of response");
                        input = scanner.nextLine();
                        offset = -count;
                        continue;
                    }
                    offset -= count;
                } else if (input.equals("E")) {
                    break;
                } else {
                    printer.display("Wrong choice");
                    input = scanner.nextLine();
                    continue;
                }

                resp = apiClientReceiver.call(createPagedCommand(offset, count));

                if (resp.isSuccess()) {
                    var showRes = (PagingAPICommand.DefaultCollectionCommandResponse) resp;
                    printer.display(showRes.getUserMessage());
                    isOut = false;
                } else {
                    if (resp.getStatus() == APIResponseStatus.AUTH_ERROR) {
                        return ClientCommandResponse.error(resp.getUserMessage());
                    }

                    printer.display(resp.getUserMessage());
                    if (isOut) offset -= count;
                    isOut = true;
                }
            } catch (APIClientException e) {
                offset = prevOffset;
                printer.display("Server error. Try later.");
            }
            printer.display("D- предыдущий U- следующий E- закончить команду");
            input = scanner.nextLine();
        }
        return ClientCommandResponse.ok();
    }


}
