package pw.stamina.mandate.internal.command.execution;

import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.result.Execution;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public interface CommandExecutor {
    Execution execute(final CommandExecutable executable, final Deque<String> arguments);
}
