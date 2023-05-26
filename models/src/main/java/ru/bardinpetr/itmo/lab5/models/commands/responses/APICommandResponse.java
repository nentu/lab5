package ru.bardinpetr.itmo.lab5.models.commands.responses;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bardinpetr.itmo.lab5.models.commands.IAPIMessage;

/**
 * Response for command. Payload should be specified by inheriting this class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APICommandResponse implements UserPrintableAPICommandResponse, IAPIMessage {
    private APIResponseStatus status = APIResponseStatus.UNPROCESSED;
    private String textualResponse = null;

    /**
     * Instantiate Response class when responding to succeeded action with filling in payload.
     *
     * @return created response object
     */
    public static APICommandResponse ok() {
        return new APICommandResponse(APIResponseStatus.OK, "ok");
    }

    /**
     * Instantiate Response class when responding to failed action with textual message
     *
     * @param text string message to return to client
     * @return created response object
     */
    public static APICommandResponse clientError(String text) {
        return new APICommandResponse(APIResponseStatus.CLIENT_ERROR, text);
    }

    /**
     * Instantiate Response class when responding to failed action with textual message
     *
     * @return created response object
     */
    public static APICommandResponse clientError(Exception cause) {
        return clientError(cause.getMessage());
    }

    /**
     * Instantiate Response class when responding to failed action with textual message
     *
     * @param text exception message
     * @return created response object
     */
    public static APICommandResponse serverError(String text) {
        return new APICommandResponse(APIResponseStatus.CLIENT_ERROR, text);
    }

    /**
     * Instantiate Response class when responding to failed action with textual message
     *
     * @param cause exception which message will be sent to client
     * @return created response object
     */
    public static APICommandResponse serverError(Exception cause) {
        return serverError(cause.toString());
    }

    /**
     * Instantiate Response class when responding to action executor for which was not resolved on server
     *
     * @return created response object
     */
    public static APICommandResponse notFound() {
        return new APICommandResponse(APIResponseStatus.NOT_FOUND, "no such command available");
    }

    @JsonIgnore
    public boolean isSuccess() {
        return status == APIResponseStatus.OK;
    }

    @JsonIgnore
    public boolean isResolved() {
        return status != APIResponseStatus.NOT_FOUND;
    }

    @Override
    public String getUserMessage() {
        return textualResponse != null ? textualResponse : "status: %s".formatted(status.toString());
    }
}
