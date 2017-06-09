package pw.stamina.mandate.io.raw;

/**
 * @author Mark Johnson
 */
@FunctionalInterface
public interface RawInputDevice {
    int read() throws RawIOException;

    default int read(final byte b[]) throws RawIOException {
        return read(b, 0, b.length);
    }

    default int read(final byte b[], final int off, final int len) throws RawIOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        for (; i < len ; i++) {
            c = read();
            if (c == -1) {
                break;
            }
            b[off + i] = (byte)c;
        }
        return i;
    }
}