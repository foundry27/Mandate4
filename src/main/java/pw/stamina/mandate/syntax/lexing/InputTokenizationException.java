package pw.stamina.mandate.syntax.lexing;

/**
 * @author Mark Johnson
 */
public class InputTokenizationException extends RuntimeException {
    public InputTokenizationException() {
        super();
    }

    public InputTokenizationException(final String message) {
        super(message);
    }

    public InputTokenizationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InputTokenizationException(final Throwable cause) {
        super(cause);
    }

    protected InputTokenizationException(final String message, final Throwable cause,
                                         final boolean enableSuppression,
                                         final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
