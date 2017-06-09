package pw.stamina.mandate.internal.argument.parsers.text.character;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.SimpleCommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class PrimitiveTypeWrappingCharacterArgumentParserDecorator implements ArgumentParser<Character> {

    private final ArgumentParser<Character> backingParser;

    public PrimitiveTypeWrappingCharacterArgumentParserDecorator(final ArgumentParser<Character> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Character> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        return backingParser.parse(arguments, wrapCommandParameterIfTypeIsPrimitive(parameter));
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(wrapCommandParameterIfTypeIsPrimitive(parameter));
    }

    private static CommandParameter wrapCommandParameterIfTypeIsPrimitive(final CommandParameter parameter) {
        return new SimpleCommandParameter(
                parameter.getRawType() == Character.TYPE ? Character.class : parameter.getRawType(),
                parameter.getTypeParameters(),
                parameter.getAnnotations(),
                parameter.findFlag().orElse(null),
                parameter.isImplicit(),
                parameter.isVarargs());
    }
}
