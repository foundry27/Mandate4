package pw.stamina.mandate.io;

import pw.stamina.mandate.io.builders.input.InputBuilder;
import pw.stamina.mandate.io.builders.output.OutputBuilder;
import pw.stamina.mandate.io.raw.RawIO;

/**
 * @author Mark Johnson
 */
public interface StandardIO {
    OutputBuilder<String> out();

    InputBuilder<String> in();

    OutputBuilder<String> err();

    static StandardIO from(final RawIO rawIO) {
        return StandardIOFactory.from(rawIO);
    }
}
