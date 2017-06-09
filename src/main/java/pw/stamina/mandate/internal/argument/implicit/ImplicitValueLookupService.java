package pw.stamina.mandate.internal.argument.implicit;

import pw.stamina.mandate.execution.CommandParameter;

/**
 * @author Mark Johnson
 */
public interface ImplicitValueLookupService {
    Object getImplicitValueForParameter(final CommandParameter parameter) throws ImplicitValueNotPresentException;
}
