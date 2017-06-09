package pw.stamina.mandate.execution.result;

import java.util.concurrent.TimeUnit;

/**
 * @author Mark Johnson
 */
public interface Execution {
    ExitCode getExitCode();

    ExitCode getExitCode(long timeout, TimeUnit timeUnit);

    boolean isComplete();

    boolean kill();
}
