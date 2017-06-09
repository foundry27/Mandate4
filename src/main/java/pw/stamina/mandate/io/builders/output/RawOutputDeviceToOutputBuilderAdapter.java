package pw.stamina.mandate.io.builders.output;

import pw.stamina.mandate.io.adapter.RawIOAdapters;
import pw.stamina.mandate.io.raw.RawOutputDevice;

import java.io.PrintStream;

/**
 * @author Mark Johnson
 */
public class RawOutputDeviceToOutputBuilderAdapter<T> implements OutputBuilder<T> {

    private final PrintStream stream;

    private String lineEnding;

    public RawOutputDeviceToOutputBuilderAdapter(final RawOutputDevice outputDevice) {
        this.stream = new PrintStream(RawIOAdapters.toOutputStream(outputDevice));
    }

    @Override
    public void write(final T output) {
        stream.print(output);
        if (lineEnding == null) {
            stream.print(System.lineSeparator());
        } else if (!lineEnding.isEmpty()) {
            stream.print(lineEnding);
        }
        stream.flush();
    }

    @Override
    public OutputBuilder<T> withLineEnding(final String lineEnding) {
        this.lineEnding = lineEnding;
        return this;
    }
}
