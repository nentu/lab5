package ru.bardinpetr.itmo.lab5.client.controller.common.handlers;

/**
 * Response class on local command execution
 *
 * @param isSuccess if request was successful
 * @param message   textual message
 * @param payload   payload or response
 * @param <T>       response payload type
 */
public record ClientCommandResponse<T>(boolean isSuccess, String message, T payload) {
    public static <T> ClientCommandResponse<T> ok() {
        return new ClientCommandResponse<>(true, null, null);
    }

    public static <T> ClientCommandResponse<T> error(String msg) {
        return new ClientCommandResponse<>(false, msg, null);
    }
}
