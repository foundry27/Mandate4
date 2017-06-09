package pw.stamina.mandate.internal.command.location;

import pw.stamina.mandate.execution.executable.CommandExecutable;

/**
 * @author Mark Johnson
 */
public abstract class CommandLookup {

    private CommandLookup() {}

    public abstract boolean wasSuccessful();

    public abstract CommandExecutable get();

    public abstract String failureMessage();

    static final class Success extends CommandLookup {

        private final CommandExecutable executable;

        Success(final CommandExecutable executable) {
            this.executable = executable;
        }

        @Override
        public boolean wasSuccessful() {
            return true;
        }

        @Override
        public CommandExecutable get() {
            return executable;
        }

        @Override
        public String failureMessage() {
            return null;
        }
    }

    static final class Failure extends CommandLookup {

        private final String failureMessage;

        Failure(final String failureMessage) {
            this.failureMessage = failureMessage;
        }

        @Override
        public boolean wasSuccessful() {
            return false;
        }

        @Override
        public CommandExecutable get() {
            return null;
        }

        @Override
        public String failureMessage() {
            return failureMessage;
        }
    }
}
