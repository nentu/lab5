package ru.bardinpetr.itmo.lab5.common.executor;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.common.executor.operations.NoReturnOperation;
import ru.bardinpetr.itmo.lab5.common.executor.operations.Operation;
import ru.bardinpetr.itmo.lab5.models.commands.requests.APICommand;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APIResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class for mapping client Commands to local implementations of these commands.
 * Register handlers with registerOperation
 */
@Slf4j
public class Executor {
    public static int MAX_RECURSION_DEPTH = 10;

    private final Map<Class<? extends APICommand>, Operation<APICommand, APICommandResponse>> operationMap = new HashMap<>();
    private final List<Executor> childExecutors = new ArrayList<>();

    /**
     * Register function "{@code operation}" for replying on "{@code cmdClass}" calls.
     *
     * @param cmdClass  class of input command object
     * @param operation function taking input Command and returning Response object
     * @param <T>       type of command
     */
    public <T extends APICommand> void registerOperation(Class<T> cmdClass, Operation<T, APICommandResponse> operation) {
        @SuppressWarnings("unchecked") var baseOperation = (Operation<APICommand, APICommandResponse>) operation;
        operationMap.put(cmdClass, baseOperation);
    }

    /**
     * Register function "{@code operation}" for replying on "{@code cmdClass}" calls.
     * Used for commands that do not have specific response type
     *
     * @param cmdClass  class of input command object
     * @param operation function taking input Command
     * @param <T>       type of command
     */
    public <T extends APICommand> void registerVoidOperation(Class<T> cmdClass, NoReturnOperation<T> operation) {
        var baseOperation = (Operation<APICommand, APICommandResponse>) (cmd) -> {
            @SuppressWarnings("unchecked") var command = (T) cmd;
            operation.apply(command);
            return cmd.createResponse();
        };
        operationMap.put(cmdClass, baseOperation);
    }

    /**
     * Join other executor with this so locally not resolved commands will be forwarded to this executor
     *
     * @param child Executor to pass commands to
     */
    public void registerExecutor(Executor child) {
        childExecutors.add(child);
    }

    /**
     * Call handler for specified command object.
     * If no handler registered returns error in Response.
     *
     * @param cmd Command from client
     * @return Response for this command or Response(success=false) if no handler exist
     */
    public APICommandResponse execute(APICommand cmd) {
        return execute(cmd, MAX_RECURSION_DEPTH);
    }

    protected APICommandResponse execute(APICommand cmd, int recursionDepth) {
        log.debug("Executing {} at {}", cmd.getCmdIdentifier(), getClass().getSimpleName());

        var op = operationMap.get(cmd.getClass());
        if (op == null) {
            log.debug("Execution of {} delegated from {}", cmd.getCmdIdentifier(), getClass().getSimpleName());

            if (recursionDepth > 0) {
                for (Executor e : childExecutors) {
                    var res = e.execute(cmd, recursionDepth - 1);
                    if (res.isResolved()) return res;
                }
            }
            return APICommandResponse.notFound();
        }
        try {
            var validation = cmd.validate();
            if (!validation.isAllowed())
                return APICommandResponse.clientError("Validation failed: %s".formatted(validation.getMsg()));
            var resp = op.apply(cmd);
            resp.setStatus(APIResponseStatus.OK);
            return resp;
        } catch (Exception ex) {
            return APICommandResponse.clientError(ex.getMessage());
        }
    }

    /**
     * Execute script (sequence of commands) one by one.
     * Errors won't stop execution.
     *
     * @param cmds commands
     * @return list of response payloads for each command in input order
     */
    public List<APICommandResponse> executeBatch(List<APICommand> cmds) {
        var result = new ArrayList<APICommandResponse>();
        boolean anyFailed = false;
        for (var cmd : cmds) {
            if (anyFailed) {
                result.add(APICommandResponse.clientError("%s skipped".formatted(cmd.getCmdIdentifier())));
                continue;
            }
            var resp = execute(cmd);
            if (!resp.isSuccess()) anyFailed = true;
            result.add(resp);
        }
        return result;
    }
}
