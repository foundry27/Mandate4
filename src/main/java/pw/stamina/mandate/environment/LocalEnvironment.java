package pw.stamina.mandate.environment;

import pw.stamina.mandate.environment.implicit.ImplicitValueProvider;

import java.util.Collection;

/**
 * @author Mark Johnson
 */
public interface LocalEnvironment extends ImplicitValueProvider {
    <T> T getLocalVariable(String variableName);

    void setLocalVariable(String variableName, Object value);

    Collection<EnvironmentVariable> getLocalVariables();
}
