package pw.stamina.mandate.internal.argument;

import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;

import java.util.Collection;

/**
 * @author Mark Johnson
 */
public class SimpleArgumentParserLookupService implements ArgumentParserLookupService {

    private final Collection<ArgumentParser<?>> argumentParsers;

    public SimpleArgumentParserLookupService(final Collection<ArgumentParser<?>> argumentParsers) {
        this.argumentParsers = argumentParsers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ArgumentParser<T> getArgumentParserForParameter(final CommandParameter parameter) throws ArgumentParserNotPresentException {
        return (ArgumentParser<T>) argumentParsers.stream()
                .filter(parser -> parser.canParseForParameter(parameter))
                .findFirst()
                .orElseThrow(() -> new ArgumentParserNotPresentException("No argument parser matching parameter " + parameter.getTypeName() + " is present"));
    }
}
