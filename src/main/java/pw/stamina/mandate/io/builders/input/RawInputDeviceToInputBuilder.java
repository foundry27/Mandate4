package pw.stamina.mandate.io.builders.input;

import pw.stamina.mandate.io.adapter.RawIOAdapters;
import pw.stamina.mandate.io.raw.RawIOException;
import pw.stamina.mandate.io.raw.RawInputDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Mark Johnson
 */
public class RawInputDeviceToInputBuilder implements InputBuilder<String> {

    private final BufferedReader reader;

    private boolean isRaw;

    private int charLimit = -1;

    public RawInputDeviceToInputBuilder(final RawInputDevice inputDevice) {
        this.reader = new BufferedReader(new InputStreamReader(RawIOAdapters.toInputStream(inputDevice)));
    }

    @Override
    public DelimitableInputBuilder<String, String[]> asRaw() {
        isRaw = true;
        return this;
    }

    @Override
    public DelimitableInputBuilder<String, String[]> withCharLimit(final int limit) {
        if (limit >= 0) {
            charLimit = limit;
        }
        return this;
    }

    @Override
    public DelimitableInputBuilder<Optional<String>, String[]> withTimeout(final long timeout, final TimeUnit unit) {
        return null;
    }

    @Override
    public String get() {
        try {
            String input = reader.readLine();
            if (charLimit >= 0) {
                input = input.substring(0, Math.min(input.length(), charLimit));
            }
            return input;
        } catch (final IOException e) {
            throw new RawIOException(e);
        }
    }

    @Override
    public BaseInputBuilder<String[]> delimitingBy(final String delimiter) {
        return null;
    }
}
