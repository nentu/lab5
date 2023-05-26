package ru.bardinpetr.itmo.lab5.common.executor.operations;

import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;

/**
 * Server side implementation of clients Command without response
 *
 * @param <T> Command to respond on
 */
public interface NoReturnOperation<T extends APICommand> {
    void apply(T cmd);
}
