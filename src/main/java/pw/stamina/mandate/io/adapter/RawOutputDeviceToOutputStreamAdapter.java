package pw.stamina.mandate.io.adapter;

import pw.stamina.mandate.io.raw.RawIOException;
import pw.stamina.mandate.io.raw.RawOutputDevice;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Mark Johnson
 */
public final class RawOutputDeviceToOutputStreamAdapter extends OutputStream {

    private final RawOutputDevice outputDevice;

    public RawOutputDeviceToOutputStreamAdapter(final RawOutputDevice outputDevice) {
        this.outputDevice = outputDevice;
    }

    @Override
    public void write(final int b) throws IOException {
        try {
            outputDevice.write(b);
        } catch (final RawIOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(final byte b[]) throws IOException {
        try {
            outputDevice.write(b, 0, b.length);
        } catch (final RawIOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(final byte b[], final int off, final int len) throws IOException {
        try {
            outputDevice.write(b, off, len);
        } catch (final RawIOException e) {
            throw new IOException(e);
        }
    }
}
