package ru.bardinpetr.itmo.lab5.network.app.server.models.requests;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.network.app.server.models.Session;

/**
 * Envelope for handling request during application chain processing
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppRequest {
    private ReqStatus status = ReqStatus.UNKNOWN;
    private Long id;
    private Session<?> session;
    private AppResponseController<?> response;
    private APICommand payload;
    private boolean isTerminated = false;


    /**
     * Check if message is still could be processed and was not sent earlier
     */
    public boolean isTerminated() {
        return isTerminated;
    }

    /**
     * Mark response as terminated and prevent further processing
     */
    public void terminate() {
        isTerminated = true;
    }

    public ReqStatus status() {
        return status;
    }

    public Long id() {
        return id;
    }

    public Session<?> session() {
        return session;
    }

    public void setSession(Session<?> session) {
        this.session = session;
    }

    public AppResponseController<?> response() {
        return response;
    }

    public APICommand payload() {
        return payload;
    }

    public void setResponse(AppResponseController<?> response) {
        this.response = response;
    }

    public void setStatus(ReqStatus newStatus) {
        status = newStatus;
    }

    public enum ReqStatus {
        UNKNOWN,
        CREATED,
        INVALID,
        NORMAL
    }
}
