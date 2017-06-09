package pw.stamina.mandate.io.raw;

/**
 * @author Mark Johnson
 */
public interface RawIODevice extends RawInputDevice, RawOutputDevice {
    int getID();
}