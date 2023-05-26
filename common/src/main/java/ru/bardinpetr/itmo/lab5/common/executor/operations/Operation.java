package ru.bardinpetr.itmo.lab5.common.executor.operations;

import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;

/**
 * Server side implementation of clients Command
 *
 * @param <T> Command to respond on
 * @param <V> Response class of function
 */
public interface Operation<T extends APICommand, V extends APICommandResponse> {
    V apply(T cmd);
}
