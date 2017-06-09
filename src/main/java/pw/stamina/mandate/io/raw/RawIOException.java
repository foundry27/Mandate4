package pw.stamina.mandate.io.raw;

/**
 * @author Mark Johnson
 */
public class RawIOException extends RuntimeException {
    public RawIOException() {
        super();
    }

    public RawIOException(final String message) {
        super(message);
    }

    public RawIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RawIOException(final Throwable cause) {
        super(cause);
    }

    protected RawIOException(final String message, final Throwable cause,
                             final boolean enableSuppression,
                             final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
