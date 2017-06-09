package pw.stamina.mandate.internal.argument;

import pw.stamina.mandate.execution.executable.ArgumentManifest;
import pw.stamina.mandate.execution.executable.CommandExecutable;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public interface CommandArgumentCollectorService {
    ArgumentManifest collectArgumentsToObjects(CommandExecutable command, Deque<String> arguments) throws ParameterDefinitionException;
}
