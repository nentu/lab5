package ru.bardinpetr.itmo.lab5.network.transport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.bardinpetr.itmo.lab5.network.transport.models.interfaces.IIdentifiableMessage;

import java.io.Serializable;

/**
 * Low level message object to passed over channel
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"response", "request"})
public class SocketMessage implements Serializable, IIdentifiableMessage {
    private CommandType cmdType;
    private Long id;
    private Long replyId;
    /**
     * If payload equals to empty array it means that the message was received in a worng way
     */
    private byte[] payload;

    public SocketMessage(CommandType type) {
        this(type, 0L, 0L, null);
    }

    public SocketMessage(byte[] payload) {
        this(CommandType.DATA, 0L, 0L, payload);
    }

    public SocketMessage() {
    }

    public SocketMessage(CommandType type, Long id, Long replyId) {
        this(type, id, replyId, null);
    }

    /**
     * @return true if this message is a DATA response for other DATA request
     */
    public boolean isResponse() {
        return cmdType == CommandType.DATA && replyId > 0;
    }

    /**
     * @return true if this message is a DATA request
     */
    public boolean isRequest() {
        return cmdType == CommandType.DATA && replyId <= 0;
    }

    public enum CommandType {
        INIT, HALT, ACK, NACK, DATA
    }
}
