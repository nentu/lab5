package ru.bardinpetr.itmo.lab5.client.ui.cli.utils;

import ru.bardinpetr.itmo.lab5.client.controller.common.handlers.UICallableCommand;

import java.util.List;

public record InvocationHistoryItem(UICallableCommand command, List<String> args) {
}
