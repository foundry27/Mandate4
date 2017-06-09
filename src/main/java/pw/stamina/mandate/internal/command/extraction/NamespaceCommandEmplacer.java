package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;

/**
 * @author Mark Johnson
 */
public interface NamespaceCommandEmplacer<T> {
    void emplaceCommandsInNamespace(T t, MutableCommandNamespace namespace) throws CommandEmplacementException;
}
