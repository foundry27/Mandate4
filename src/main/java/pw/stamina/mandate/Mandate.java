package pw.stamina.mandate;

import pw.stamina.mandate.internal.SimpleCommandSystem;

/**
 * @author Mark Johnson
 */
public final class Mandate {
    private Mandate() {}

    public static CommandSystem newCommandSystem() {
        return new SimpleCommandSystem();
    }
}
