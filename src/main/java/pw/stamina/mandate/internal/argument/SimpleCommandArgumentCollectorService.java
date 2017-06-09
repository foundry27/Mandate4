package pw.stamina.mandate.internal.argument;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.executable.ArgumentManifest;
import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.executable.SimpleArgumentManifest;
import pw.stamina.mandate.internal.argument.implicit.ImplicitValueLookupService;

import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark Johnson
 */
public class SimpleCommandArgumentCollectorService implements CommandArgumentCollectorService {

    private final ImplicitValueLookupService implicitValueLookupService;

    private final ExplicitArgumentResolutionService explicitArgumentResolutionService;

    public SimpleCommandArgumentCollectorService(final ImplicitValueLookupService implicitValueLookupService,
                                                 final ExplicitArgumentResolutionService explicitArgumentResolutionService) {
        this.implicitValueLookupService = implicitValueLookupService;
        this.explicitArgumentResolutionService = explicitArgumentResolutionService;
    }

    @Override
    public ArgumentManifest collectArgumentsToObjects(final CommandExecutable command, final Deque<String> arguments) throws ParameterDefinitionException {
        final List<Object> collectedArguments = command.getParameters().stream()
                .map(parameter -> getCorrectArgumentForParameter(parameter, arguments))
                .collect(Collectors.toList());
        return new SimpleArgumentManifest(collectedArguments);
    }

    private Object getCorrectArgumentForParameter(final CommandParameter parameter, final Deque<String> arguments) {
        if (parameter.isImplicit()) {
            return implicitValueLookupService.getImplicitValueForParameter(parameter);
        } else {
            return explicitArgumentResolutionService.getExplicitArgumentForParameter(parameter, arguments);
        }
    }

}
