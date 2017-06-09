package pw.stamina.mandate.internal.argument.parsers.enumeration;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public enum SimpleEnumArgumentParser implements ArgumentParser<Enum<?>> {
    INSTANCE;

    @Override
    public Try<Enum<?>> parse(final Deque<String> arguments, final CommandParameter parameter) {
        final String input = arguments.pop();
        for (final Enum<?> enumConstant : (Enum<?>[]) parameter.getRawType().getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(input) || enumConstant.toString().equalsIgnoreCase(input)) {
                return Try.success(enumConstant);
            }
        }
        return Try.failure(new IllegalInputException("'" + input + "' does not map to a valid enum constant in " + parameter.getRawType().getCanonicalName()));
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return parameter.getRawType().isEnum();
    }
}
