package pw.stamina.mandate;

/**
 * @author Mark Johnson
 */
public interface CommandPath {
    void register(Object container);

    void unregister(Object container);
}
