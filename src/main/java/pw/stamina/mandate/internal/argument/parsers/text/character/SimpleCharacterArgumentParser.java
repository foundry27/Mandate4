package pw.stamina.mandate.internal.argument.parsers.text.character;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public enum SimpleCharacterArgumentParser implements ArgumentParser<Character> {
    INSTANCE;

    @Override
    public Try<Character> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final String input = arguments.pop();
        if (input.length() == 1) {
            return Try.success(input.charAt(0));
        } else {
            return Try.failure(new IllegalInputException("Input must be a single character: got '" + input + "' instead"));
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return Character.class == parameter.getRawType();
    }
}
