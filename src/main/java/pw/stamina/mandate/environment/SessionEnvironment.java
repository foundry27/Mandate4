package pw.stamina.mandate.environment;

import java.util.Collection;

/**
 * @author Mark Johnson
 */
public interface SessionEnvironment {
    Collection<EnvironmentVariable> getSessionVariables();

    <T> T getSessionVariable(String variableName);

    void setSessionVariable(String variableName, Object value);
}
