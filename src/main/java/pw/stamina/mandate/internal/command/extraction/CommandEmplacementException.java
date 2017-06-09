package pw.stamina.mandate.internal.command.extraction;

/**
 * @author Mark Johnson
 */
public class CommandEmplacementException extends RuntimeException {
    public CommandEmplacementException() {
        super();
    }

    public CommandEmplacementException(final String message) {
        super(message);
    }

    public CommandEmplacementException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandEmplacementException(final Throwable cause) {
        super(cause);
    }

    protected CommandEmplacementException(final String message, final Throwable cause,
                                          final boolean enableSuppression,
                                          final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
