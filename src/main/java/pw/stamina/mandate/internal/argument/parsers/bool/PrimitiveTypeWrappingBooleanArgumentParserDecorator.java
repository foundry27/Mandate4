package pw.stamina.mandate.internal.argument.parsers.bool;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.SimpleCommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class PrimitiveTypeWrappingBooleanArgumentParserDecorator implements ArgumentParser<Boolean> {

    private final ArgumentParser<Boolean> backingParser;

    public PrimitiveTypeWrappingBooleanArgumentParserDecorator(final ArgumentParser<Boolean> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Boolean> parse(final Deque<String> arguments, final CommandParameter parameter) {
        return backingParser.parse(arguments, wrapCommandParameterIfTypeIsPrimitive(parameter));
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(wrapCommandParameterIfTypeIsPrimitive(parameter));
    }

    private static CommandParameter wrapCommandParameterIfTypeIsPrimitive(final CommandParameter parameter) {
        return new SimpleCommandParameter(
                parameter.getRawType() == Boolean.TYPE ? Boolean.class : parameter.getRawType(),
                parameter.getTypeParameters(),
                parameter.getAnnotations(),
                parameter.findFlag().orElse(null),
                parameter.isImplicit(),
                parameter.isVarargs());
    }
}
