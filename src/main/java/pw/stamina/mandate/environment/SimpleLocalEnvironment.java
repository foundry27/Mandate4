package pw.stamina.mandate.environment;

import pw.stamina.mandate.environment.implicit.ImplicitValueProvider;
import pw.stamina.mandate.execution.CommandParameter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark Johnson
 */
public class SimpleLocalEnvironment implements LocalEnvironment {

    private final Map<String, Object> localVariables;

    final Collection<ImplicitValueProvider> implicitProviders;

    public SimpleLocalEnvironment(final Collection<ImplicitValueProvider> implicitProviders) {
        this.localVariables = new HashMap<>();
        this.implicitProviders = implicitProviders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getLocalVariable(final String variableName) {
        return (T) localVariables.get(variableName);
    }

    @Override
    public void setLocalVariable(final String variableName, final Object value) {
        localVariables.put(variableName, value);
    }

    @Override
    public Collection<EnvironmentVariable> getLocalVariables() {
        return localVariables.entrySet().stream()
                .map(entry -> new EnvironmentVariable(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean canProvideForParameter(final CommandParameter parameter) {
        for (final ImplicitValueProvider p : implicitProviders) {
            if (p.canProvideForParameter(parameter)) return true;
        }
        return false;
    }

    @Override
    public Object getValueForParameter(final CommandParameter parameter) {
        for (final ImplicitValueProvider p : implicitProviders) {
            if (p.canProvideForParameter(parameter)) return p.getValueForParameter(parameter);
        }
        return null;
    }
}
