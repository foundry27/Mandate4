package pw.stamina.mandate.execution.executable;

/**
 * @author Mark Johnson
 */
public class CommandExecutionException extends RuntimeException {
    public CommandExecutionException() {
        super();
    }

    public CommandExecutionException(final String message) {
        super(message);
    }

    public CommandExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandExecutionException(final Throwable cause) {
        super(cause);
    }

    protected CommandExecutionException(final String message, final Throwable cause,
                                        final boolean enableSuppression,
                                        final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
