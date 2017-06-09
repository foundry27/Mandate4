package pw.stamina.mandate.internal.argument.parsers.number;

import io.vavr.control.Try;
import pw.stamina.mandate.annotations.numeric.RealClamp;
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
public final class RealValueClampingNumberArgumentParserDecorator implements ArgumentParser<Number> {

    private static final Map<Class<? extends Number>, Function<RealClamp, UnaryOperator<Number>>> REAL_CLAMP_ROUNDING_FUNCTIONS;

    private static final Map<Class<? extends Number>, Function<RealClamp, Consumer<Number>>> REAL_CLAMP_NUMBER_VALIDATORS;

    private final ArgumentParser<Number> backingParser;

    public RealValueClampingNumberArgumentParserDecorator(final ArgumentParser<Number> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Number> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final Try<Number> parsedNumber = backingParser.parse(arguments, parameter);
        return parsedNumber.mapTry(n -> clampNumberIfNecessary(n, parameter));
    }

    private static Number clampNumberIfNecessary(final Number number, final CommandParameter parameter) throws IllegalInputException {
        if (parameter.isAnnotationPresent(RealClamp.class)) {
            return clampNumberToRealClamp(number, parameter.getAnnotation(RealClamp.class));
        } else {
            return number;
        }
    }

    private static Number clampNumberToRealClamp(final Number number, final RealClamp realClamp) {
        switch (realClamp.type()) {
            case COERCE: return coerceNumberToRealClamp(number, realClamp);
            case VALIDATE: validateNumberWithinRealClamp(number, realClamp);
            default: return number;
        }
    }

    private static Number coerceNumberToRealClamp(final Number number, final RealClamp realClamp) throws IllegalInputException {
        final Function<RealClamp, UnaryOperator<Number>> processorLookup = REAL_CLAMP_ROUNDING_FUNCTIONS.get(number.getClass());
        if (processorLookup != null) {
            return processorLookup.apply(realClamp).apply(number);
        } else {
            throw new IllegalInputException("No RealClamp processor exists for numbers of type " + number.getClass().getCanonicalName());
        }
    }

    private static void validateNumberWithinRealClamp(final Number number, final RealClamp realClamp) {
        final Function<RealClamp, Consumer<Number>> validatorLookup = REAL_CLAMP_NUMBER_VALIDATORS.get(number.getClass());
        if (validatorLookup != null) {
            validatorLookup.apply(realClamp).accept(number);
        } else {
            throw new IllegalInputException("No RealClamp validator exists for numbers of type " + number.getClass().getCanonicalName());
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(parameter);
    }

    static {
        final Map<Class<? extends Number>, Function<RealClamp, UnaryOperator<Number>>> processors = new HashMap<>(8, 100);
        processors.put(Byte.class, realClamp -> number -> (byte) Math.max(Math.min(number.byteValue(), realClamp.max()), realClamp.min()));
        processors.put(Short.class, realClamp -> number -> (short) Math.max(Math.min(number.shortValue(), realClamp.max()), realClamp.min()));
        processors.put(Integer.class, realClamp -> number -> (int) Math.max(Math.min(number.intValue(), realClamp.max()), realClamp.min()));
        processors.put(Long.class, realClamp -> number -> (long) Math.max(Math.min(number.longValue(), realClamp.max()), realClamp.min()));
        processors.put(Float.class, realClamp -> number -> (float) Math.max(Math.min(number.floatValue(), realClamp.max()), realClamp.min()));
        processors.put(Double.class, realClamp -> number -> (double) Math.max(Math.min(number.doubleValue(), realClamp.max()), realClamp.min()));
        processors.put(BigInteger.class, realClamp -> number -> ((BigInteger) number).min(BigInteger.valueOf((long) realClamp.max())).max(BigInteger.valueOf((long) realClamp.min())));
        processors.put(BigDecimal.class, realClamp -> number -> ((BigDecimal) number).min(BigDecimal.valueOf(realClamp.max())).max(BigDecimal.valueOf(realClamp.min())));
        REAL_CLAMP_ROUNDING_FUNCTIONS = Collections.unmodifiableMap(processors);
    }

    static {
        final Map<Class<? extends Number>, Function<RealClamp, Consumer<Number>>> validators = new HashMap<>(8, 100);
        validators.put(Byte.class, realClamp -> number -> ensureNumberWithinBounds(number.byteValue(), (byte) realClamp.min(), (byte) realClamp.max()));
        validators.put(Short.class, realClamp -> number -> ensureNumberWithinBounds(number.shortValue(), (short) realClamp.min(), (short) realClamp.max()));
        validators.put(Integer.class, realClamp -> number -> ensureNumberWithinBounds(number.intValue(), (int) realClamp.min(), (int) realClamp.max()));
        validators.put(Long.class, realClamp -> number -> ensureNumberWithinBounds(number.longValue(), (long) realClamp.min(), (long) realClamp.max()));
        validators.put(Float.class, realClamp -> number -> ensureNumberWithinBounds(number.floatValue(), (float) realClamp.min(), (float) realClamp.max()));
        validators.put(Double.class, realClamp -> number -> ensureNumberWithinBounds(number.doubleValue(), realClamp.min(), realClamp.max()));
        validators.put(BigInteger.class, realClamp -> number -> ensureNumberWithinBounds((BigInteger) number, BigInteger.valueOf((long) realClamp.min()), BigInteger.valueOf((long) realClamp.max())));
        validators.put(BigInteger.class, realClamp -> number -> ensureNumberWithinBounds((BigDecimal) number, BigDecimal.valueOf(realClamp.min()), BigDecimal.valueOf(realClamp.max())));
        REAL_CLAMP_NUMBER_VALIDATORS = validators;
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
