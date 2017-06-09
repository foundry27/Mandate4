package pw.stamina.mandate.internal.command.extraction;

/**
 * @author Mark Johnson
 */
public class CommandRemovalException extends RuntimeException {
    public CommandRemovalException() {
        super();
    }

    public CommandRemovalException(final String message) {
        super(message);
    }

    public CommandRemovalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandRemovalException(final Throwable cause) {
        super(cause);
    }

    protected CommandRemovalException(final String message, final Throwable cause,
                                      final boolean enableSuppression,
                                      final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
