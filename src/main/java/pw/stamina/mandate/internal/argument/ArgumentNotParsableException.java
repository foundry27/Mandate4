package pw.stamina.mandate.internal.argument;

/**
 * @author Mark Johnson
 */
public class ArgumentNotParsableException extends ParameterDefinitionException {
    public ArgumentNotParsableException() {
        super();
    }

    public ArgumentNotParsableException(final String message) {
        super(message);
    }

    public ArgumentNotParsableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ArgumentNotParsableException(final Throwable cause) {
        super(cause);
    }

    protected ArgumentNotParsableException(final String message, final Throwable cause,
                                           final boolean enableSuppression,
                                           final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
