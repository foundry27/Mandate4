package pw.stamina.mandate;

import pw.stamina.mandate.execution.result.Execution;

/**
 * @author Mark Johnson
 */
public interface CommandShell {
    Execution execute(String command);
}
