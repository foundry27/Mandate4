package pw.stamina.mandate.internal.argument.implicit;

import pw.stamina.mandate.environment.implicit.ImplicitValueProvider;
import pw.stamina.mandate.execution.CommandParameter;

import java.util.Collection;

/**
 * @author Mark Johnson
 */
public class SimpleImplicitValueLookupService implements ImplicitValueLookupService {

    private final Collection<ImplicitValueProvider> implicitValueProviders;

    public SimpleImplicitValueLookupService(final Collection<ImplicitValueProvider> implicitValueProviders) {
        this.implicitValueProviders = implicitValueProviders;
    }

    @Override
    public Object getImplicitValueForParameter(final CommandParameter parameter) throws ImplicitValueNotPresentException {
        return implicitValueProviders.stream()
                .filter(provider -> provider.canProvideForParameter(parameter))
                .findFirst()
                .orElseThrow(() -> new ImplicitValueNotPresentException("No implicit value matching parameter " + parameter.getTypeName() + " is present"))
                .getValueForParameter(parameter);
    }
}
