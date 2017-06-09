package pw.stamina.mandate.execution.namespace;

import pw.stamina.mandate.execution.executable.CommandExecutable;

/**
 * @author Mark Johnson
 */
public interface MutableCommandNamespace extends CommandNamespace {
    void addCommand(CommandExecutable executable);

    void addSubNamespace(CommandNamespace namespace);

    void removeCommandByName(String executableName);

    void removeSubNamespaceByName(String namespaceName);
}
