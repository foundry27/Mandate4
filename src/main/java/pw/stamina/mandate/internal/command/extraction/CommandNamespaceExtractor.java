package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.execution.namespace.CommandNamespace;

/**
 * @author Mark Johnson
 */
public interface CommandNamespaceExtractor<T> {
    CommandNamespace extractNamespace(T t);
}
