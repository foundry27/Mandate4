package pw.stamina.mandate.internal.argument.parsers.number;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author Mark Johnson
 */
public enum SimpleNumberArgumentParser implements ArgumentParser<Number> {
    INSTANCE;

    private static final Predicate<String> HEX_VALIDATOR_PREDICATE = Pattern.compile("^([-+])?(0x|0X|#)[a-fA-F0-9]+$").asPredicate();

    private static final Predicate<String> OCTAL_VALIDATOR_PREDICATE = Pattern.compile("^([-+])?0[1-7][0-7]*$").asPredicate();

    private static final Map<Class<? extends Number>, Function<String, Number>> NUMBER_DECODERS;

    @Override
    public Try<Number> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final Function<String, Number> decoderLookup = NUMBER_DECODERS.get(parameter.getRawType());
        if (decoderLookup != null) {
            final String input = arguments.pop();
            try {
                return Try.success(decoderLookup.apply(input));
            } catch (final NumberFormatException e) {
                return Try.failure(new IllegalInputException("'" + input + "' cannot be parsed to a(n) " + parameter.getRawType().getCanonicalName(), e));
            }
        } else {
            return Try.failure(new IllegalInputException("Numbers of type " + parameter.getRawType().getCanonicalName() + " are not supported at this time"));
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return NUMBER_DECODERS.containsKey(parameter.getRawType());
    }

    private static Double decodeDouble(final String input) throws NumberFormatException {
        return (HEX_VALIDATOR_PREDICATE.test(input) || OCTAL_VALIDATOR_PREDICATE.test(input))
                ? Long.decode(input).doubleValue()
                : Double.valueOf(input);
    }

    private static Float decodeFloat(final String input) throws NumberFormatException {
        return (HEX_VALIDATOR_PREDICATE.test(input) || OCTAL_VALIDATOR_PREDICATE.test(input))
                ? Integer.decode(input).floatValue()
                : Float.valueOf(input);
    }

    private static BigInteger decodeBigInteger(final String input) throws NumberFormatException {
        return HEX_VALIDATOR_PREDICATE.test(input)
                ? new BigInteger((input.charAt(0) == '0' ? input.substring(2) : input.substring(1)), 16)
                : new BigInteger(input, (OCTAL_VALIDATOR_PREDICATE.test(input) ? 8 : 10));
    }

    private static BigDecimal decodeBigDecimal(final String input) throws NumberFormatException {
        return HEX_VALIDATOR_PREDICATE.test(input)
                ? new BigDecimal(new BigInteger((input.charAt(0) == '0' ? input.substring(2) : input.substring(1)), 16))
                : (OCTAL_VALIDATOR_PREDICATE.test(input)
                ? new BigDecimal(new BigInteger(input, 8))
                : new BigDecimal(input));
    }

    static {
        final Map<Class<? extends Number>, Function<String, Number>> decoders = new HashMap<>();
        decoders.put(Byte.class, Byte::decode);
        decoders.put(Short.class, Short::decode);
        decoders.put(Integer.class, Integer::decode);
        decoders.put(Long.class, Long::decode);
        decoders.put(Double.class, SimpleNumberArgumentParser::decodeDouble);
        decoders.put(Float.class, SimpleNumberArgumentParser::decodeFloat);
        decoders.put(BigInteger.class, SimpleNumberArgumentParser::decodeBigInteger);
        decoders.put(BigDecimal.class, SimpleNumberArgumentParser::decodeBigDecimal);
        NUMBER_DECODERS = Collections.unmodifiableMap(decoders);
    }
}
