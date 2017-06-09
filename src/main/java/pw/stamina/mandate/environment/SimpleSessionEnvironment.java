package pw.stamina.mandate.environment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark Johnson
 */
public class SimpleSessionEnvironment implements SessionEnvironment {

    private final Map<String, Object> sessionVariables;

    public SimpleSessionEnvironment() {
        this.sessionVariables = new HashMap<>();
    }

    @Override
    public Collection<EnvironmentVariable> getSessionVariables() {
        return sessionVariables.entrySet().stream()
                .map(entry -> new EnvironmentVariable(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSessionVariable(final String variableName) {
        return (T) sessionVariables.get(variableName);
    }

    @Override
    public void setSessionVariable(final String variableName, final Object value) {
        sessionVariables.put(variableName, value);
    }
}
