package pw.stamina.mandate.internal.argument;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;

/**
 * @author Mark Johnson
 */
public interface ArgumentParserLookupService {
    <T> ArgumentParser<T> getArgumentParserForParameter(final CommandParameter parameter) throws ArgumentParserNotPresentException;
}
