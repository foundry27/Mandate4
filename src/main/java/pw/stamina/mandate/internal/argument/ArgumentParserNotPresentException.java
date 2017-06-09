package pw.stamina.mandate.internal.argument;

/**
 * @author Mark Johnson
 */
public class ArgumentParserNotPresentException extends ParameterDefinitionException {
    public ArgumentParserNotPresentException() {
        super();
    }

    public ArgumentParserNotPresentException(final String message) {
        super(message);
    }

    public ArgumentParserNotPresentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ArgumentParserNotPresentException(final Throwable cause) {
        super(cause);
    }

    protected ArgumentParserNotPresentException(final String message, final Throwable cause,
                                                final boolean enableSuppression,
                                                final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
