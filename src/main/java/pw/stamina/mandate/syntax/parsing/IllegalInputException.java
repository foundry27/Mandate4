package pw.stamina.mandate.syntax.parsing;

/**
 * @author Mark Johnson
 */
public class IllegalInputException extends RuntimeException {
    public IllegalInputException() {
        super();
    }

    public IllegalInputException(final String message) {
        super(message);
    }

    public IllegalInputException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IllegalInputException(final Throwable cause) {
        super(cause);
    }

    protected IllegalInputException(final String message, final Throwable cause,
                                    final boolean enableSuppression,
                                    final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
