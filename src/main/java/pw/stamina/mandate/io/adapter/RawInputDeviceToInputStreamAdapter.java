package pw.stamina.mandate.io.adapter;

import pw.stamina.mandate.io.raw.RawIOException;
import pw.stamina.mandate.io.raw.RawInputDevice;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mark Johnson
 */
public final class RawInputDeviceToInputStreamAdapter extends InputStream {

    private final RawInputDevice inputDevice;

    public RawInputDeviceToInputStreamAdapter(final RawInputDevice inputDevice) {
        this.inputDevice = inputDevice;
    }

    @Override
    public int read() throws IOException {
        try {
            return inputDevice.read();
        } catch (final RawIOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(final byte b[]) throws IOException {
        try {
            return inputDevice.read(b, 0, b.length);
        } catch (final RawIOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(final byte b[], final int off, final int len) throws IOException {
        try {
            return inputDevice.read(b, off, len);
        } catch (final RawIOException e) {
            throw new IOException(e);
        }
    }
}
