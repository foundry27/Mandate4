package pw.stamina.mandate.internal.command.location;

import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.namespace.CommandNamespace;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class NamespaceScanningCommandLocator implements CommandLocator {

    private final CommandNamespace rootNamespace;

    public NamespaceScanningCommandLocator(final CommandNamespace rootNamespace) {
        this.rootNamespace = rootNamespace;
    }

    @Override
    public CommandLookup locateCommandMatchingInput(final Deque<String> tokens) {
        CommandNamespace workingNamespace = rootNamespace;
        for (String token = tokens.pollFirst(); token != null; token = tokens.pollFirst()) {
            final CommandExecutable executableLookup = workingNamespace.getSubCommand(token);
            if (executableLookup != null) {
                return new CommandLookup.Success(executableLookup);
            } else{
                final CommandNamespace namespaceLookup = workingNamespace.getSubNamespace(token);
                if (namespaceLookup != null) {
                    workingNamespace = namespaceLookup;
                } else {
                    return new CommandLookup.Failure("No command or namespace matching '" + token + "' was found");
                }
            }
        }
        return new CommandLookup.Failure("Unable to locate command matching input. Remaining tokens: " + tokens);
    }
}
