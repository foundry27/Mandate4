package pw.stamina.mandate.internal.command.execution;

import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.result.Execution;
import pw.stamina.mandate.internal.argument.CommandArgumentCollectorService;
import pw.stamina.mandate.security.SystemUser;

import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author Mark Johnson
 */
public final class SimpleCommandExecutor implements CommandExecutor {

    private final SystemUser executingUser;

    private final CommandArgumentCollectorService argumentCollectorService;

    private final Consumer<Exception> exceptionConsumer;

    public SimpleCommandExecutor(final SystemUser executingUser,
                                 final CommandArgumentCollectorService argumentCollectorService,
                                 final Consumer<Exception> exceptionConsumer) {
        this.executingUser = executingUser;
        this.argumentCollectorService = argumentCollectorService;
        this.exceptionConsumer = exceptionConsumer;
    }

    @Override
    public Execution execute(final CommandExecutable executable, final Deque<String> arguments) {
        return new DeferringAsynchronousCommandExecution(
                callable -> Executors.newSingleThreadExecutor().submit(callable),
                () -> executable.execute(executingUser, argumentCollectorService.collectArgumentsToObjects(executable, arguments)),
                exceptionConsumer);
    }
}
