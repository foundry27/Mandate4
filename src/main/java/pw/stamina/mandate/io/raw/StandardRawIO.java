package pw.stamina.mandate.io.raw;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Johnson
 */
public enum StandardRawIO implements RawIO {
    INSTANCE;

    private final Map<Integer, RawIODevice> deviceCache = new HashMap<>();

    @Override
    public RawIODevice getDevice(final int deviceID) {
        switch (deviceID) {
            case 0:
                return StandardRawIODevice.OUT;
            case 1:
                return StandardRawIODevice.IN;
            case 2:
                return StandardRawIODevice.ERR;
            default:
                   return deviceCache.computeIfAbsent(deviceID, ByteQueueRawIODevice::new);
        }
    }
}
