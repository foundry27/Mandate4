package pw.stamina.mandate.environment.implicit;

import pw.stamina.mandate.execution.CommandParameter;

/**
 * @author Mark Johnson
 */
public interface ImplicitValueProvider {
    boolean canProvideForParameter(CommandParameter parameter);

    Object getValueForParameter(CommandParameter parameter);
}
