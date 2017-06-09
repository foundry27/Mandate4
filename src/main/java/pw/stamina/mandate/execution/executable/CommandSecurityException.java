package pw.stamina.mandate.execution.executable;

/**
 * @author Mark Johnson
 */
public class CommandSecurityException extends RuntimeException {
    public CommandSecurityException() {
        super();
    }

    public CommandSecurityException(final String message) {
        super(message);
    }

    public CommandSecurityException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandSecurityException(final Throwable cause) {
        super(cause);
    }

    protected CommandSecurityException(final String message, final Throwable cause,
                                       final boolean enableSuppression,
                                       final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
