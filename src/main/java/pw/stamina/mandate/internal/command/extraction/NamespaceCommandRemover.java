package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;

/**
 * @author Mark Johnson
 */
public interface NamespaceCommandRemover<T> {
    void removeCommandsInNamespace(T t, MutableCommandNamespace namespace) throws CommandRemovalException;
}