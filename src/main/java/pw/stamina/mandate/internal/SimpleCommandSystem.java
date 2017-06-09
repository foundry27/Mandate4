package pw.stamina.mandate.internal;

import pw.stamina.mandate.CommandSystem;
import pw.stamina.mandate.SystemSession;
import pw.stamina.mandate.security.SystemUser;

/**
 * @author Mark Johnson
 */
public final class SimpleCommandSystem implements CommandSystem {
    @Override
    public SystemSession getSession(final SystemUser user) {
        return new SimpleSystemSession(user);
    }
}
