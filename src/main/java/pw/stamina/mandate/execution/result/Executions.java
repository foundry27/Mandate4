package pw.stamina.mandate.execution.result;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Mark Johnson
 */
public final class Executions {

    private static final Map<ExitCode, Execution> COMPLETED_EXECUTIONS_FOR_EXITCODES = new HashMap<>();

    private Executions() {}

    public static Execution complete(final ExitCode exitCode) {
        return COMPLETED_EXECUTIONS_FOR_EXITCODES.computeIfAbsent(exitCode, CompleteExecution::new);
    }

    private static final class CompleteExecution implements Execution {

        private final ExitCode exitCode;

        CompleteExecution(final ExitCode exitCode) {
            this.exitCode = exitCode;
        }

        @Override
        public ExitCode getExitCode() {
            return exitCode;
        }

        @Override
        public ExitCode getExitCode(final long timeout, final TimeUnit timeUnit) {
            return exitCode;
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean kill() {
            return false;
        }
    }

}
