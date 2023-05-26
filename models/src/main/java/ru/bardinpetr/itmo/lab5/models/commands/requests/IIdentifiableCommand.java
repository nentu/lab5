package ru.bardinpetr.itmo.lab5.models.commands.requests;

/**
 * Interface to make any command identifiable from outside
 *
 * @param <K> identifier type
 */
public interface IIdentifiableCommand<K> {
    /**
     * @return any textual identifier that could uniquely distinguish that command type
     */
    K getCmdIdentifier();
}
