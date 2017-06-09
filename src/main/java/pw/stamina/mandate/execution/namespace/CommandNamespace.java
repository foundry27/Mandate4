package pw.stamina.mandate.execution.namespace;

import pw.stamina.mandate.execution.executable.CommandExecutable;

import java.util.Optional;

/**
 * @author Mark Johnson
 */
public interface CommandNamespace {
    Optional<CommandNamespace> findParentNamespace();

    String getName();

    CommandExecutable getSubCommand(String command);

    CommandNamespace getSubNamespace(String namespace);
}
