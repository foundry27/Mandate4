package pw.stamina.mandate.io.raw;

/**
 * @author Mark Johnson
 */
public interface RawIO {
    RawIODevice getDevice(int deviceID);
}
