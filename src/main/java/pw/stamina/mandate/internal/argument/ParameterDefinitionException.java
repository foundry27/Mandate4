package pw.stamina.mandate.internal.argument;

/**
 * @author Mark Johnson
 */
public class ParameterDefinitionException extends RuntimeException {
    public ParameterDefinitionException() {
        super();
    }

    public ParameterDefinitionException(final String message) {
        super(message);
    }

    public ParameterDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ParameterDefinitionException(final Throwable cause) {
        super(cause);
    }

    protected ParameterDefinitionException(final String message, final Throwable cause,
                                           final boolean enableSuppression,
                                           final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
