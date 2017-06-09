package pw.stamina.mandate.io;

import pw.stamina.mandate.io.raw.RawIO;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mark Johnson
 */
final class StandardIOFactory {
    private static final Map<RawIO, StandardIO> cachedIO = new WeakHashMap<>();

    private StandardIOFactory() {}

    static StandardIO from(final RawIO rawIO) {
        return cachedIO.computeIfAbsent(rawIO, SimpleStandardIO::new);
    }
}
