package pw.stamina.mandate.internal.command.execution;

import pw.stamina.mandate.execution.result.Execution;
import pw.stamina.mandate.execution.result.ExitCode;
import pw.stamina.mandate.execution.result.ExitCodes;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Mark Johnson
 */
public final class DeferringAsynchronousCommandExecution implements Execution {

    private final Future<ExitCode> pendingResult;

    private final Consumer<Exception> exceptionConsumer;

    public DeferringAsynchronousCommandExecution(final Function<Callable<ExitCode>, Future<ExitCode>> executor, final Callable<ExitCode> callable, final Consumer<Exception> exceptionConsumer) {
        this.pendingResult = executor.apply(callable);
        this.exceptionConsumer = exceptionConsumer;
    }

    @Override
    public ExitCode getExitCode() {
        try {
            return pendingResult.get();
        } catch (final InterruptedException e) {
            return ExitCodes.INTERRUPTED;
        } catch (final ExecutionException e) {
            exceptionConsumer.accept(e);
            return ExitCodes.FAILURE;
        }
    }

    @Override
    public ExitCode getExitCode(final long timeout, final TimeUnit timeUnit) {
        try {
            return pendingResult.get(timeout, timeUnit);
        } catch (final InterruptedException | TimeoutException e) {
            return ExitCodes.INTERRUPTED;
        } catch (final ExecutionException e) {
            return ExitCodes.FAILURE;
        }
    }

    @Override
    public boolean isComplete() {
        return pendingResult.isDone();
    }

    @Override
    public boolean kill() {
        return pendingResult.cancel(true);
    }
}
