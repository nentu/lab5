package ru.bardinpetr.itmo.lab5.models.commands.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.bardinpetr.itmo.lab5.models.commands.IAPIMessage;
import ru.bardinpetr.itmo.lab5.models.commands.responses.APICommandResponse;
import ru.bardinpetr.itmo.lab5.models.data.validation.ValidationResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * General class for all commands
 */
@JsonIgnoreProperties({"type", "cmdIdentifier", "inlineArgs", "interactArgs"})
@Data
public abstract class APICommand implements IIdentifiableCommand<Class<? extends APICommand>>, IAPIMessage {

    private final Map<String, Object> header = new HashMap<>();

    @Override
    public Class<? extends APICommand> getCmdIdentifier() {
        return getClass();
    }

    /**
     * @return respond for certain command
     */
    public APICommandResponse createResponse() {
        return APICommandResponse.ok();
    }

    /**
     * validation method
     */
    public ValidationResponse validate() {
        return new ValidationResponse(true, "OK");
    }
}
