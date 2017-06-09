package pw.stamina.mandate.environment.implicit;

import pw.stamina.mandate.execution.CommandParameter;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Mark Johnson
 */
public final class DeferringImplicitValueProvider implements ImplicitValueProvider {

    private final Predicate<CommandParameter> provisionPredicate;

    private final Function<CommandParameter, Object> providerFunction;

    public DeferringImplicitValueProvider(final Predicate<CommandParameter> provisionPredicate,
                                          final Function<CommandParameter, Object> providerFunction) {
        this.provisionPredicate = provisionPredicate;
        this.providerFunction = providerFunction;
    }

    @Override
    public boolean canProvideForParameter(final CommandParameter parameter) {
        return provisionPredicate.test(parameter);
    }

    @Override
    public Object getValueForParameter(final CommandParameter parameter) {
        return canProvideForParameter(parameter) ? providerFunction.apply(parameter) : null;
    }
}
