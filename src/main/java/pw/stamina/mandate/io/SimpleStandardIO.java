package pw.stamina.mandate.io;

import pw.stamina.mandate.io.builders.input.InputBuilder;
import pw.stamina.mandate.io.builders.input.RawInputDeviceToInputBuilder;
import pw.stamina.mandate.io.builders.output.OutputBuilder;
import pw.stamina.mandate.io.builders.output.RawOutputDeviceToOutputBuilderAdapter;
import pw.stamina.mandate.io.raw.RawIO;

/**
 * @author Mark Johnson
 */
final class SimpleStandardIO implements StandardIO {

    private final RawIO io;

    SimpleStandardIO(final RawIO io) {
        this.io = io;
    }

    @Override
    public OutputBuilder<String> out() {
        return new RawOutputDeviceToOutputBuilderAdapter<>(io.getDevice(0));
    }

    @Override
    public InputBuilder<String> in() {
        return new RawInputDeviceToInputBuilder(io.getDevice(1));
    }

    @Override
    public OutputBuilder<String> err() {
        return new RawOutputDeviceToOutputBuilderAdapter<>(io.getDevice(2));
    }
}
