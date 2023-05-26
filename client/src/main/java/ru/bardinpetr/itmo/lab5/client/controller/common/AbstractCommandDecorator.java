package ru.bardinpetr.itmo.lab5.client.controller.common;

import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.ClientCommandResponse;

import java.util.Map;

/**
 * Base decorator for commands
 */
public class AbstractCommandDecorator extends AbstractLocalCommand {
    protected final AbstractLocalCommand decoratee;

    public AbstractCommandDecorator(AbstractLocalCommand decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public ClientCommandResponse<?> execute(String cmdName, Map<String, Object> args) {
        return decoratee.execute(cmdName, args);
    }
}
