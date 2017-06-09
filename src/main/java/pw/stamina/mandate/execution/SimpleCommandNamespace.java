package pw.stamina.mandate.execution;

import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.namespace.CommandNamespace;
import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mark Johnson
 */
public class SimpleCommandNamespace implements MutableCommandNamespace {

    private final CommandNamespace parentNamespace;

    private final String namespaceName;

    private final Map<String, CommandExecutable> commandExecutables;

    private final Map<String, CommandNamespace> commandNamespaces;

    public SimpleCommandNamespace(final MutableCommandNamespace parentNamespace, final String namespaceName) {
        this.parentNamespace = parentNamespace;
        this.namespaceName = namespaceName;
        this.commandExecutables = new HashMap<>();
        this.commandNamespaces = new HashMap<>();
        if (parentNamespace != null) {
            parentNamespace.addSubNamespace(this);
        }
    }

    @Override
    public Optional<CommandNamespace> findParentNamespace() {
        return Optional.ofNullable(parentNamespace);
    }

    @Override
    public String getName() {
        return namespaceName;
    }

    @Override
    public CommandExecutable getSubCommand(final String command) {
        final CommandExecutable lookup = commandExecutables.get(command);
        if (lookup != null) {
            return lookup;
        } else {
            return null;
        }
    }

    @Override
    public CommandNamespace getSubNamespace(final String namespace) {
        final CommandNamespace lookup = commandNamespaces.get(namespace);
        if (lookup != null) {
            return lookup;
        } else {
            return null;
        }
    }

    @Override
    public void addCommand(final CommandExecutable executable) {
        this.commandExecutables.put(executable.getName(), executable);
    }

    @Override
    public void addSubNamespace(final CommandNamespace namespace) {
        this.commandNamespaces.put(namespace.getName(), namespace);
    }

    @Override
    public void removeCommandByName(final String executableName) {
        this.commandExecutables.remove(executableName);
    }

    @Override
    public void removeSubNamespaceByName(final String namespaceName) {
        this.commandNamespaces.remove(namespaceName);
    }
}
