package pw.stamina.mandate;

import pw.stamina.mandate.security.SystemUser;

/**
 * @author Mark Johnson
 */
public interface CommandSystem {
    SystemSession getSession(SystemUser user);
}
