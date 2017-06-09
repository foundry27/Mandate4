package pw.stamina.mandate.io.raw;

import java.io.IOException;

/**
 * @author Mark Johnson
 */
public enum StandardRawIODevice implements RawIODevice {
    OUT {
        @Override
        public int read() throws RawIOException {
            throw new UnsupportedOperationException("Cannot write to the standard output stream");
        }

        @Override
        public int getID() {
            return 0;
        }

        @Override
        public void write(final int b) {
            System.out.write(b);
        }
    },
    IN {
        @Override
        public int read() throws RawIOException {
            try {
                return System.in.read();
            } catch (final IOException e) {
                throw new RawIOException(e);
            }
        }

        @Override
        public int read(final byte b[]) throws RawIOException {
            try {
                return System.in.read(b);
            } catch (final IOException e) {
                throw new RawIOException(e);
            }
        }

        @Override
        public int read(final byte b[], final int off, final int len) throws RawIOException {
            try {
                return System.in.read(b, off, len);
            } catch (final IOException e) {
                throw new RawIOException(e);
            }
        }

        @Override
        public int getID() {
            return 1;
        }

        @Override
        public void write(final int b) {
            throw new UnsupportedOperationException("Cannot write to the standard input stream");
        }
    },
    ERR {
        @Override
        public int read() throws RawIOException {
            throw new UnsupportedOperationException("Cannot write to the standard error stream");
        }

        @Override
        public int getID() {
            return 2;
        }

        @Override
        public void write(final int b) {
            System.err.write(b);;
        }
    };
}
