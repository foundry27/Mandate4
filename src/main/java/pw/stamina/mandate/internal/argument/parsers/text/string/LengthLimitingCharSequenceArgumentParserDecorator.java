package pw.stamina.mandate.internal.argument.parsers.text.string;

import io.vavr.control.Try;
import pw.stamina.mandate.annotations.validation.Sized;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class LengthLimitingCharSequenceArgumentParserDecorator implements ArgumentParser<CharSequence> {

    private final ArgumentParser<CharSequence> backingParser;

    public LengthLimitingCharSequenceArgumentParserDecorator(final ArgumentParser<CharSequence> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<CharSequence> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final String argumentLookahead = arguments.peek();
        validateLengthIfNecessary(argumentLookahead, parameter);
        return backingParser.parse(arguments, parameter);
    }

    private static void validateLengthIfNecessary(final String input, final CommandParameter parameter) throws IllegalInputException {
        final Sized sizeClamp = parameter.getAnnotation(Sized.class);
        if (sizeClamp != null) {
            final int minimumLength = Math.min(sizeClamp.min(), sizeClamp.max());
            final int maximumLength = Math.max(sizeClamp.min(), sizeClamp.max());
            if (input.length() < minimumLength) {
                throw new IllegalInputException(String.format("'%s' is too short: length can be between %d-%d characters", input, minimumLength, maximumLength));
            } else if (input.length() > maximumLength) {
                throw new IllegalInputException(String.format("'%s' is too long: length can be between %d-%d characters", input, minimumLength, maximumLength));
            }
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(parameter);
    }
}
