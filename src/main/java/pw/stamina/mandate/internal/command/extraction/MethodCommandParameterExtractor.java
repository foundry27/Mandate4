package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.ReflectionParameterToCommandParameterAdapter;
import pw.stamina.mandate.execution.flag.FlagGroupLookupService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark Johnson
 */
public enum MethodCommandParameterExtractor implements CommandParameterExtractor<Method> {
    INSTANCE;

    @Override
    public List<CommandParameter> getCommandParameters(final Method method) {
        final FlagGroupLookupService flagGroupLookupService = new FlagGroupLookupService();
        return Arrays.stream(method.getParameters())
                .map(param -> new ReflectionParameterToCommandParameterAdapter(param, flagGroupLookupService))
                .collect(Collectors.toList());
    }
}
