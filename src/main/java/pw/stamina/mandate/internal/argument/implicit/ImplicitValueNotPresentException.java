package pw.stamina.mandate.internal.argument.implicit;

import pw.stamina.mandate.internal.argument.ParameterDefinitionException;

/**
 * @author Mark Johnson
 */
public class ImplicitValueNotPresentException extends ParameterDefinitionException {
    public ImplicitValueNotPresentException() {
        super();
    }

    public ImplicitValueNotPresentException(final String message) {
        super(message);
    }

    public ImplicitValueNotPresentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ImplicitValueNotPresentException(final Throwable cause) {
        super(cause);
    }

    protected ImplicitValueNotPresentException(final String message, final Throwable cause,
                                               final boolean enableSuppression,
                                               final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
