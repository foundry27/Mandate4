package pw.stamina.mandate.internal.argument.parsers.number;

import io.vavr.control.Try;
import pw.stamina.mandate.annotations.numeric.IntClamp;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Mark Johnson
 */
public final class IntegerValueClampingNumberArgumentParserDecorator implements ArgumentParser<Number> {

    private static final Map<Class<? extends Number>, Function<IntClamp, UnaryOperator<Number>>> INT_CLAMP_ROUNDING_FUNCTIONS;

    private static final Map<Class<? extends Number>, Function<IntClamp, Consumer<Number>>> INT_CLAMP_NUMBER_VALIDATORS;

    private final ArgumentParser<Number> backingParser;

    public IntegerValueClampingNumberArgumentParserDecorator(final ArgumentParser<Number> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Number> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final Try<Number> parsedNumber = backingParser.parse(arguments, parameter);
        return parsedNumber.mapTry(n -> clampNumberIfNecessary(n, parameter));
    }

    private static Number clampNumberIfNecessary(final Number number, final CommandParameter parameter) throws IllegalInputException {
        if (parameter.isAnnotationPresent(IntClamp.class)) {
            return clampNumberToIntClamp(number, parameter.getAnnotation(IntClamp.class));
        } else {
            return number;
        }
    }

    private static Number clampNumberToIntClamp(final Number number, final IntClamp intClamp) {
        switch (intClamp.type()) {
            case COERCE: return coerceNumberToIntClamp(number, intClamp);
            case VALIDATE: validateNumberWithinIntClamp(number, intClamp);
            default: return number;
        }
    }

    private static Number coerceNumberToIntClamp(final Number number, final IntClamp intClamp) throws IllegalInputException {
        final Function<IntClamp, UnaryOperator<Number>> processorLookup = INT_CLAMP_ROUNDING_FUNCTIONS.get(number.getClass());
        if (processorLookup != null) {
            return processorLookup.apply(intClamp).apply(number);
        } else {
            throw new IllegalInputException("No IntClamp processor exists for numbers of type " + number.getClass().getCanonicalName());
        }
    }

    private static void validateNumberWithinIntClamp(final Number number, final IntClamp intClamp) {
        final Function<IntClamp, Consumer<Number>> validatorLookup = INT_CLAMP_NUMBER_VALIDATORS.get(number.getClass());
        if (validatorLookup != null) {
            validatorLookup.apply(intClamp).accept(number);
        } else {
            throw new IllegalInputException("No IntClamp validator exists for numbers of type " + number.getClass().getCanonicalName());
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(parameter);
    }

    static {
        final Map<Class<? extends Number>, Function<IntClamp, UnaryOperator<Number>>> processors = new HashMap<>(8, 100);
        processors.put(Byte.class, intClamp -> number -> (byte) Math.max(Math.min(number.byteValue(), intClamp.max()), intClamp.min()));
        processors.put(Short.class, intClamp -> number -> (short) Math.max(Math.min(number.shortValue(), intClamp.max()), intClamp.min()));
        processors.put(Integer.class, intClamp -> number -> (int) Math.max(Math.min(number.intValue(), intClamp.max()), intClamp.min()));
        processors.put(Long.class, intClamp -> number -> (long) Math.max(Math.min(number.longValue(), intClamp.max()), intClamp.min()));
        processors.put(Float.class, intClamp -> number -> (float) Math.max(Math.min(number.floatValue(), intClamp.max()), intClamp.min()));
        processors.put(Double.class, intClamp -> number -> (double) Math.max(Math.min(number.doubleValue(), intClamp.max()), intClamp.min()));
        processors.put(BigInteger.class, intClamp -> number -> ((BigInteger) number).min(BigInteger.valueOf(intClamp.max())).max(BigInteger.valueOf(intClamp.min())));
        processors.put(BigDecimal.class, intClamp -> number -> ((BigDecimal) number).min(BigDecimal.valueOf(intClamp.max())).max(BigDecimal.valueOf(intClamp.min())));
        INT_CLAMP_ROUNDING_FUNCTIONS = Collections.unmodifiableMap(processors);
    }

    static {
        final Map<Class<? extends Number>, Function<IntClamp, Consumer<Number>>> validators = new HashMap<>(8, 100);
        validators.put(Byte.class, intClamp -> number -> ensureNumberWithinBounds(number.byteValue(), (byte) intClamp.min(), (byte) intClamp.max()));
        validators.put(Short.class, intClamp -> number -> ensureNumberWithinBounds(number.shortValue(), (short) intClamp.min(), (short) intClamp.max()));
        validators.put(Integer.class, intClamp -> number -> ensureNumberWithinBounds(number.intValue(), (int) intClamp.min(), (int) intClamp.max()));
        validators.put(Long.class, intClamp -> number -> ensureNumberWithinBounds(number.longValue(), intClamp.min(), intClamp.max()));
        validators.put(Float.class, intClamp -> number -> ensureNumberWithinBounds(number.floatValue(), (float) intClamp.min(), (float) intClamp.max()));
        validators.put(Double.class, intClamp -> number -> ensureNumberWithinBounds(number.doubleValue(), (double) intClamp.min(), (double) intClamp.max()));
        validators.put(BigInteger.class, intClamp -> number -> ensureNumberWithinBounds((BigInteger) number, BigInteger.valueOf(intClamp.min()), BigInteger.valueOf(intClamp.max())));
        validators.put(BigInteger.class, intClamp -> number -> ensureNumberWithinBounds((BigDecimal) number, BigDecimal.valueOf(intClamp.min()), BigDecimal.valueOf(intClamp.max())));
        INT_CLAMP_NUMBER_VALIDATORS = validators;
    }

    private static <T extends Number & Comparable<? super T>> void ensureNumberWithinBounds(final T number, final T min, final T max) {
        if (max != null && number.compareTo(max) > 0) {
            throw new IllegalInputException("Input number " + number + " must be less than or equal to " + max);
        }
        if (min != null && number.compareTo(min) < 0) {
            throw new IllegalInputException("Input number " + number + " must be greater than or equal to " + min);
        }
    }

}
