package pw.stamina.mandate.internal.argument;

import pw.stamina.mandate.annotations.flag.AutoFlag;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.flag.FlagGroup;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mark Johnson
 */
public class SimpleExplicitArgumentResolutionService implements ExplicitArgumentResolutionService {

    private final ArgumentParserLookupService argumentParserLookupService;

    public SimpleExplicitArgumentResolutionService(final ArgumentParserLookupService argumentParserLookupService) {
        this.argumentParserLookupService = argumentParserLookupService;
    }

    @Override
    public Object getExplicitArgumentForParameter(final CommandParameter parameter, final Deque<String> arguments) throws ArgumentNotParsableException {
        final ArgumentParser<?> argumentParser = argumentParserLookupService.getArgumentParserForParameter(parameter);
        final Set<FlagGroup> flagGroupsPresent = new HashSet<>();

        if (parameter.isAnnotationPresent(AutoFlag.class)) {
            final AutoFlag autoFlag = parameter.getAnnotation(AutoFlag.class);

        }
        return argumentParser.parse(arguments, parameter).getOrElseThrow(t -> new ArgumentNotParsableException(t));
    }
}
