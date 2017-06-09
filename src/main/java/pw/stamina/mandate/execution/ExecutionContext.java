package pw.stamina.mandate.execution;

import pw.stamina.mandate.SystemSession;
import pw.stamina.mandate.environment.LocalEnvironment;
import pw.stamina.mandate.io.raw.RawIO;

/**
 * @author Mark Johnson
 */
public interface ExecutionContext {
    SystemSession getCurrentSession();

    LocalEnvironment getLocalEnvironment();

    RawIO getIOHandle();
}
