package pw.stamina.mandate.internal.command.execution.executable;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.executable.ArgumentManifest;
import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.executable.CommandExecutionException;
import pw.stamina.mandate.execution.executable.CommandSecurityException;
import pw.stamina.mandate.execution.namespace.CommandNamespace;
import pw.stamina.mandate.execution.result.ExitCode;
import pw.stamina.mandate.internal.security.PermissionTag;
import pw.stamina.mandate.security.Permission;
import pw.stamina.mandate.security.SystemUser;

import java.util.List;

/**
 * @author Mark Johnson
 */
public final class PermissionValidatingCommandExecutableDecorator implements CommandExecutable {

    private final List<PermissionTag> permissions;

    private final CommandExecutable backingExecutable;

    public PermissionValidatingCommandExecutableDecorator(final List<PermissionTag> permissions, final CommandExecutable backingExecutable) {
        this.permissions = permissions;
        this.backingExecutable = backingExecutable;
    }

    @Override
    public ExitCode execute(final SystemUser systemUser, final ArgumentManifest argumentManifest) throws CommandExecutionException, CommandSecurityException {
        if (canExecute(systemUser)) {
            return backingExecutable.execute(systemUser, argumentManifest);
        } else {
            throw new CommandSecurityException("User does not have permission to execute this command");
        }
    }

    @Override
    public String getName() {
        return backingExecutable.getName();
    }

    @Override
    public CommandNamespace getNamespace() {
        return backingExecutable.getNamespace();
    }

    @Override
    public List<CommandParameter> getParameters() {
        return backingExecutable.getParameters();
    }

    @Override
    public boolean canExecute(final SystemUser user) {
        OUTER_LOOP: for (final PermissionTag permissionTag : permissions) {
            if (!user.hasPermission(permissionTag.getExpectedPermission())) {
                for (final Permission alternatePermission : permissionTag.getAlternatePermissions()) {
                    if (user.hasPermission(alternatePermission)) {
                        continue OUTER_LOOP;
                    }
                }
                return false;
            }
        }
        return true;
    }
}
