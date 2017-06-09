package pw.stamina.mandate.internal.argument.parsers.number;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.SimpleCommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Johnson
 */
public final class PrimitiveTypeWrappingNumberArgumentParserDecorator implements ArgumentParser<Number> {

    private static final Map<Class<?>, Class<?>> UNWRAPPED_TO_WRAPPED;

    private final ArgumentParser<Number> backingParser;

    public PrimitiveTypeWrappingNumberArgumentParserDecorator(final ArgumentParser<Number> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Number> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        return backingParser.parse(arguments, wrapCommandParameterIfTypeIsPrimitive(parameter));
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(wrapCommandParameterIfTypeIsPrimitive(parameter));
    }

    private static CommandParameter wrapCommandParameterIfTypeIsPrimitive(final CommandParameter parameter) {
        return new SimpleCommandParameter(
                UNWRAPPED_TO_WRAPPED.getOrDefault(parameter.getRawType(), parameter.getRawType()),
                parameter.getTypeParameters(),
                parameter.getAnnotations(),
                parameter.findFlag().orElse(null),
                parameter.isImplicit(),
                parameter.isVarargs());
    }

    static {
        final Map<Class<?>, Class<?>> unwrappedToWrapped = new HashMap<>(6, 100);
        unwrappedToWrapped.put(Byte.TYPE, Byte.class);
        unwrappedToWrapped.put(Short.TYPE, Short.class);
        unwrappedToWrapped.put(Integer.TYPE, Integer.class);
        unwrappedToWrapped.put(Long.TYPE, Long.class);
        unwrappedToWrapped.put(Float.TYPE, Float.class);
        unwrappedToWrapped.put(Double.TYPE, Double.class);
        UNWRAPPED_TO_WRAPPED = unwrappedToWrapped;
    }

}
