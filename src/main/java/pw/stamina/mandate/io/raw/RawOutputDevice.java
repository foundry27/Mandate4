package pw.stamina.mandate.io.raw;

/**
 * @author Mark Johnson
 */
@FunctionalInterface
public interface RawOutputDevice {
    void write(int b) throws RawIOException;

    default void write(final byte b[]) throws RawIOException {
        write(b, 0, b.length);
    }

    default void write(final byte b[], final int off, final int len) throws RawIOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }
}
