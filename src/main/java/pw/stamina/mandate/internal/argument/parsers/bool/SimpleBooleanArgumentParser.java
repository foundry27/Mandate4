package pw.stamina.mandate.internal.argument.parsers.bool;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Johnson
 */
public enum SimpleBooleanArgumentParser implements ArgumentParser<Boolean> {
    INSTANCE;

    private static final Map<String, Boolean> STRING_REPRESENTATION_MAP;

    @Override
    public Try<Boolean> parse(final Deque<String> arguments, final CommandParameter parameter) {
        final String input = arguments.pop();
        final Boolean lookup = STRING_REPRESENTATION_MAP.get(input);
        if (lookup != null) {
            return Try.success(lookup);
        } else {
            return Try.failure(new IllegalInputException("'" + input + "' does not map to a valid boolean value"));
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return Boolean.class == parameter.getRawType();
    }

    static {
        final Map<String, Boolean> lookups = new HashMap<>(12, 100);
        lookups.put("enabled", Boolean.TRUE);
        lookups.put("true", Boolean.TRUE);
        lookups.put("yes", Boolean.TRUE);
        lookups.put("on", Boolean.TRUE);
        lookups.put("y", Boolean.TRUE);
        lookups.put("1", Boolean.TRUE);
        lookups.put("disabled", Boolean.FALSE);
        lookups.put("false", Boolean.FALSE);
        lookups.put("off", Boolean.FALSE);
        lookups.put("no", Boolean.FALSE);
        lookups.put("n", Boolean.FALSE);
        lookups.put("0", Boolean.FALSE);
        STRING_REPRESENTATION_MAP = Collections.unmodifiableMap(lookups);
    }
}
