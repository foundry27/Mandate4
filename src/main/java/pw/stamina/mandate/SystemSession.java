package pw.stamina.mandate;

import pw.stamina.mandate.environment.SessionEnvironment;
import pw.stamina.mandate.io.raw.RawIO;
import pw.stamina.mandate.security.SystemUser;

/**
 * @author Mark Johnson
 */
public interface SystemSession {
    CommandPath getCommandPath();

    CommandShell makeShell(RawIO io);

    SystemUser getSessionUser();

    SessionEnvironment getSessionEnvironment();
}
