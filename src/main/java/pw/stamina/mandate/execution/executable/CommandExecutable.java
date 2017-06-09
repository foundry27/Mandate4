package pw.stamina.mandate.execution.executable;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.namespace.CommandNamespace;
import pw.stamina.mandate.execution.result.ExitCode;
import pw.stamina.mandate.security.SystemUser;

import java.util.List;

/**
 * @author Mark Johnson
 */
public interface CommandExecutable {
    ExitCode execute(SystemUser systemUser, ArgumentManifest argumentManifest) throws CommandExecutionException, CommandSecurityException;

    String getName();

    CommandNamespace getNamespace();

    List<CommandParameter> getParameters();

    default boolean canExecute(SystemUser user) {
        return true;
    }
}
