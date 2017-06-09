package pw.stamina.mandate.internal.argument.parsers.text.string;

import io.vavr.control.Try;
import pw.stamina.mandate.annotations.validation.Match;
import pw.stamina.mandate.annotations.validation.Matches;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mark Johnson
 */
public final class PatternMatchingCharSequenceArgumentParserDecorator implements ArgumentParser<CharSequence> {

    private final ArgumentParser<CharSequence> backingParser;

    public PatternMatchingCharSequenceArgumentParserDecorator(final ArgumentParser<CharSequence> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<CharSequence> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final String argumentLookahead = arguments.peek();
        validateMatchingIfNecessary(argumentLookahead, parameter);
        return backingParser.parse(arguments, parameter);
    }

    private static void validateMatchingIfNecessary(final String input, final CommandParameter parameter) throws IllegalInputException {
        final Matches matches = parameter.getAnnotation(Matches.class);
        if (matches != null) {
            for (final Match matchOption : matches.value()) {
                switch (matchOption.type()) {
                    case STRICT_EQUALITY: validateStrictEqualityMatch(input, matchOption.value()); break;
                    case CASE_INSENSITIVE_EQUALITY: validateCaseInsensitiveEqualityMatch(input, matchOption.value()); break;
                    case PARTIAL_PATTERN: validatePartialPatternMatch(input, matchOption.value()); break;
                    case FULL_PATTERN: validateFullPatternMatch(input, matchOption.value()); break;
                }
            }
        }
    }

    private static void validateStrictEqualityMatch(final String input, final String target) throws IllegalInputException {
        if (!input.equals(target))
            throw new IllegalInputException("Input '" + input + "' is not exactly equal to '" + target + "'");
    }

    private static void validateCaseInsensitiveEqualityMatch(final String input, final String target) throws IllegalInputException {
        if (!input.equalsIgnoreCase(target))
            throw new IllegalInputException("Input '" + input + "' is not case-insensitively equal to '" + target + "'");
    }


    private static void validatePartialPatternMatch(final String input, final String pattern) throws IllegalInputException {
        if (!Pattern.compile(pattern).matcher(input).find())
            throw new IllegalInputException("Input '" + input + "' does not contain any matches for pattern '" + pattern + "'");
    }

    private static void validateFullPatternMatch(final String input, final String pattern) throws IllegalInputException {
        final Matcher matcher = Pattern.compile(pattern).matcher(input);
        if (!matcher.matches()) {
            if (!matcher.find()) {
                throw new IllegalInputException("Input '" + input + "' does not contain any matches for pattern '" + pattern + "'");
            } else {
                throw new IllegalInputException("Input '" + input + "' does not fully match pattern '" + pattern + "' (partial match found)");
            }
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(parameter);
    }
}
