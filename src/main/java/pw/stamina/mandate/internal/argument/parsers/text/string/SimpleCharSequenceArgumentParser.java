package pw.stamina.mandate.internal.argument.parsers.text.string;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import javax.swing.text.Segment;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Mark Johnson
 */
public enum SimpleCharSequenceArgumentParser implements ArgumentParser<CharSequence> {
    INSTANCE;

    private static final Map<Class<?>, Function<String, ? extends CharSequence>> SEQUENCE_CONVERTERS;

    @Override
    public Try<CharSequence> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final String input = arguments.pop();
        return Try.of(() -> formatSequence(input, parameter.getRawType()));
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return SEQUENCE_CONVERTERS.containsKey(parameter.getRawType());
    }

    private static CharSequence formatSequence(final String input, final Class<?> sequenceType) throws IllegalInputException {
        final Function<String, ? extends CharSequence> sequencerLookup = SEQUENCE_CONVERTERS.get(sequenceType);
        if (sequencerLookup != null) {
            return sequencerLookup.apply(input);
        } else {
            throw new IllegalInputException("CharSequences of type " + sequenceType.getCanonicalName() + " are not supported at this time");
        }
    }

    static {
        final Map<Class<?>, Function<String, ? extends CharSequence>> seq = new HashMap<>(5, 100);
        seq.put(String.class, Function.identity());
        seq.put(StringBuilder.class, StringBuilder::new);
        seq.put(StringBuffer.class, StringBuffer::new);
        seq.put(CharBuffer.class, CharBuffer::wrap);
        seq.put(Segment.class, s -> new Segment(s.toCharArray(), 0, s.length()));
        seq.put(CharSequence.class, Function.identity());
        SEQUENCE_CONVERTERS = Collections.unmodifiableMap(seq);
    }
}
