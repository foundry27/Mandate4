package pw.stamina.mandate.internal.argument;

import pw.stamina.mandate.execution.CommandParameter;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public interface ExplicitArgumentResolutionService {
    Object getExplicitArgumentForParameter(final CommandParameter parameter, final Deque<String> arguments) throws ArgumentNotParsableException;
}
