package pw.stamina.mandate.internal.command.execution.executable;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.executable.ArgumentManifest;
import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.executable.CommandExecutionException;
import pw.stamina.mandate.execution.executable.CommandSecurityException;
import pw.stamina.mandate.execution.namespace.CommandNamespace;
import pw.stamina.mandate.execution.result.ExitCode;
import pw.stamina.mandate.security.SystemUser;

import java.util.List;
import java.util.function.Function;

/**
 * @author Mark Johnson
 */
public class DeferringCommandExecutable implements CommandExecutable {

    private final String name;

    private final CommandNamespace namespace;

    private final Function<ArgumentManifest, ExitCode> executionFunction;

    private final List<CommandParameter> commandParameters;

    public DeferringCommandExecutable(final String name, final CommandNamespace namespace,
                                      final List<CommandParameter> commandParameters,
                                      final Function<ArgumentManifest, ExitCode> executionFunction) {
        this.name = name;
        this.namespace = namespace;
        this.executionFunction = executionFunction;
        this.commandParameters = commandParameters;
    }


    @Override
    public ExitCode execute(final SystemUser systemUser, final ArgumentManifest argumentManifest) throws CommandExecutionException, CommandSecurityException {
        return executionFunction.apply(argumentManifest);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CommandNamespace getNamespace() {
        return namespace;
    }

    @Override
    public List<CommandParameter> getParameters() {
        return commandParameters;
    }

}
