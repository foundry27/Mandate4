package pw.stamina.mandate.io.adapter;

import pw.stamina.mandate.io.raw.RawInputDevice;
import pw.stamina.mandate.io.raw.RawOutputDevice;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Mark Johnson
 */
public final class RawIOAdapters {

    private RawIOAdapters() {}

    public static OutputStream toOutputStream(final RawOutputDevice device) {
        return new RawOutputDeviceToOutputStreamAdapter(device);
    }

    public static InputStream toInputStream(final RawInputDevice device) {
        return new RawInputDeviceToInputStreamAdapter(device);
    }
}
