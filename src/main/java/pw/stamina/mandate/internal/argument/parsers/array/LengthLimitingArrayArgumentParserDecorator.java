package pw.stamina.mandate.internal.argument.parsers.array;

import io.vavr.control.Try;
import pw.stamina.mandate.annotations.validation.Sized;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.lang.reflect.Array;
import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class LengthLimitingArrayArgumentParserDecorator implements ArgumentParser<Object> {

    private final ArgumentParser<Object> backingParser;

    public LengthLimitingArrayArgumentParserDecorator(final ArgumentParser<Object> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Object> parse(final Deque<String> arguments, final CommandParameter parameter) {
        final Try<Object> parsedArray = backingParser.parse(arguments, parameter);
        return parsedArray.andThenTry(o -> validateLengthIfNecessary(o, parameter));
    }

    private static void validateLengthIfNecessary(final Object array, final CommandParameter parameter) throws IllegalInputException {
        final Sized length = parameter.getAnnotation(Sized.class);
        if (length != null) {
            final int min = Math.min(length.min(), length.max());
            final int max = Math.max(length.min(), length.max());
            final int arrayLength = Array.getLength(array);
            if (arrayLength < min) {
                throw new IllegalInputException("Array is too short: length can be between " + min + "-" + max + " elements");
            } else if (arrayLength > max) {
                throw new IllegalInputException("Array is too long: length can be between " + min + "-" + max + " elements");
            }
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(parameter);
    }
}
