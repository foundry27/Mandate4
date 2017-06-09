package pw.stamina.mandate.environment;

/**
 * @author Mark Johnson
 */
public final class EnvironmentVariable {

    private final String variableName;

    private final Object value;

    public EnvironmentVariable(final String variableName, final Object value) {
        this.variableName = variableName;
        this.value = value;
    }

    public String getName() {
        return variableName;
    }

    public Object getValue() {
        return value;
    }
}
