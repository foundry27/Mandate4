package pw.stamina.mandate.internal.argument.parsers.number;

import io.vavr.control.Try;
import pw.stamina.mandate.annotations.numeric.PreciseClamp;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Mark Johnson
 */
public final class PreciseValueClampingNumberArgumentParserDecorator implements ArgumentParser<Number> {

    private static final Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Try<Number>>, UnaryOperator<Number>>> PRECISE_CLAMP_ROUNDING_FUNCTIONS;

    private static final Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Try<Number>>, Consumer<Number>>> PRECISE_CLAMP_NUMBER_VALIDATORS;

    private final ArgumentParser<Number> backingParser;

    public PreciseValueClampingNumberArgumentParserDecorator(final ArgumentParser<Number> backingParser) {
        this.backingParser = backingParser;
    }

    @Override
    public Try<Number> parse(final Deque<String> arguments, final CommandParameter parameter) throws IllegalInputException {
        final Try<Number> parsedNumber = backingParser.parse(arguments, parameter);
        return parsedNumber.mapTry(n -> clampNumberIfNecessary(n, parameter));
    }

    private Number clampNumberIfNecessary(final Number number, final CommandParameter parameter) throws IllegalInputException {
        if (parameter.isAnnotationPresent(PreciseClamp.class)) {
            return clampNumberToPreciseClamp(number, parameter.getAnnotation(PreciseClamp.class),
                    input -> backingParser.parse(new ArrayDeque<>(Collections.singleton(input)), parameter));
        } else {
            return number;
        }
    }

    private static Number clampNumberToPreciseClamp(final Number number, final PreciseClamp preciseClamp, final Function<String, Try<Number>> parser) {
        switch (preciseClamp.type()) {
            case COERCE: return coerceNumberToPreciseClamp(number, preciseClamp, parser);
            case VALIDATE: validateNumberWithinPreciseClamp(number, preciseClamp, parser);
            default: return number;
        }
    }

    private static Number coerceNumberToPreciseClamp(final Number number, final PreciseClamp preciseClamp, final Function<String, Try<Number>> parser) throws IllegalInputException {
        final BiFunction<PreciseClamp, Function<String, Try<Number>>, UnaryOperator<Number>> processorLookup = PRECISE_CLAMP_ROUNDING_FUNCTIONS.get(number.getClass());
        if (processorLookup != null) {
            return processorLookup.apply(preciseClamp, parser).apply(number);
        } else {
            throw new IllegalInputException("No PreciseClamp processor exists for numbers of type " + number.getClass().getCanonicalName());
        }
    }

    private static void validateNumberWithinPreciseClamp(final Number number, final PreciseClamp preciseClamp, final Function<String, Try<Number>> parser) throws IllegalInputException {
        final BiFunction<PreciseClamp, Function<String, Try<Number>>, Consumer<Number>> validatorLookup = PRECISE_CLAMP_NUMBER_VALIDATORS.get(number.getClass());
        if (validatorLookup != null) {
            validatorLookup.apply(preciseClamp, parser).accept(number);
        } else {
            throw new IllegalInputException("No PreciseClamp validator exists for numbers of type " + number.getClass().getCanonicalName());
        }
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return backingParser.canParseForParameter(parameter);
    }

    static {
        final Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Try<Number>>, UnaryOperator<Number>>> processors = new HashMap<>(8, 100);
        processors.put(Byte.class, (preciseClamp, parser) -> number -> {
            int num = number.byteValue();
            if (!preciseClamp.max().isEmpty()) num = Math.min(num, parser.apply(preciseClamp.max()).get().byteValue());
            if (!preciseClamp.min().isEmpty()) num = Math.max(num, parser.apply(preciseClamp.min()).get().byteValue());
            return (byte) num;
        });
        processors.put(Short.class, (preciseClamp, parser) -> number -> {
            int num = number.shortValue();
            if (!preciseClamp.max().isEmpty()) num = Math.min(num, parser.apply(preciseClamp.max()).get().shortValue());
            if (!preciseClamp.min().isEmpty()) num = Math.max(num, parser.apply(preciseClamp.min()).get().shortValue());
            return (short) num;
        });
        processors.put(Integer.class, (preciseClamp, parser) -> number -> {
            int num = number.intValue();
            if (!preciseClamp.max().isEmpty()) num = Math.min(num, parser.apply(preciseClamp.max()).get().intValue());
            if (!preciseClamp.min().isEmpty()) num = Math.max(num, parser.apply(preciseClamp.min()).get().intValue());
            return num;
        });
        processors.put(Long.class, (preciseClamp, parser) -> number -> {
            long num = number.longValue();
            if (!preciseClamp.max().isEmpty()) num = Math.min(num, parser.apply(preciseClamp.max()).get().longValue());
            if (!preciseClamp.min().isEmpty()) num = Math.max(num, parser.apply(preciseClamp.min()).get().longValue());
            return num;
        });
        processors.put(Float.class, (preciseClamp, parser) -> number -> {
            float num = number.floatValue();
            if (!preciseClamp.max().isEmpty()) num = Math.min(num, parser.apply(preciseClamp.max()).get().floatValue());
            if (!preciseClamp.min().isEmpty()) num = Math.max(num, parser.apply(preciseClamp.min()).get().floatValue());
            return num;
        });
        processors.put(Double.class, (preciseClamp, parser) -> number -> {
            double num = number.doubleValue();
            if (!preciseClamp.max().isEmpty()) num = Math.min(num, parser.apply(preciseClamp.max()).get().doubleValue());
            if (!preciseClamp.min().isEmpty()) num = Math.max(num, parser.apply(preciseClamp.min()).get().doubleValue());
            return num;
        });
        processors.put(BigInteger.class, (preciseClamp, parser) -> number -> {
            BigInteger num = (BigInteger) number;
            if (!preciseClamp.max().isEmpty()) num = num.min((BigInteger) parser.apply(preciseClamp.max()).get());
            if (!preciseClamp.min().isEmpty()) num = num.max((BigInteger) parser.apply(preciseClamp.min()).get());
            return num;
        });
        processors.put(BigDecimal.class, (preciseClamp, parser) -> number -> {
            BigDecimal num = (BigDecimal) number;
            if (!preciseClamp.max().isEmpty()) num = num.max((BigDecimal) parser.apply(preciseClamp.max()).get());
            if (!preciseClamp.min().isEmpty()) num = num.min((BigDecimal) parser.apply(preciseClamp.min()).get());
            return num;
        });
        PRECISE_CLAMP_ROUNDING_FUNCTIONS = Collections.unmodifiableMap(processors);
    }

    static {
        final Map<Class<? extends Number>, BiFunction<PreciseClamp, Function<String, Try<Number>>, Consumer<Number>>> validators = new HashMap<>(8, 100);
        validators.put(Byte.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, number.byteValue(), Number::byteValue));
        validators.put(Short.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, number.shortValue(), Number::shortValue));
        validators.put(Integer.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, number.intValue(), Number::intValue));
        validators.put(Long.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, number.longValue(), Number::longValue));
        validators.put(Float.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, number.floatValue(), Number::floatValue));
        validators.put(Double.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, number.doubleValue(), Number::doubleValue));
        validators.put(BigInteger.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, (BigInteger) number, BigInteger.class::cast));
        validators.put(BigDecimal.class, (preciseClamp, parser) -> number -> validatePreciseClamp(preciseClamp, parser, (BigDecimal) number, BigDecimal.class::cast));
        PRECISE_CLAMP_NUMBER_VALIDATORS = validators;
    }

    private static <T extends Number & Comparable<? super T>> void validatePreciseClamp(
            final PreciseClamp preciseClamp, final Function<String, Try<Number>> parser, final T inputNumber,
            final Function<Number, T> clampNumberNormalizer) {
        final Number parsedMin = !preciseClamp.min().isEmpty() ? parser.apply(preciseClamp.min()).get() : null;
        final Number parsedMax = !preciseClamp.max().isEmpty() ? parser.apply(preciseClamp.max()).get() : null;
        ensureNumberWithinBounds(inputNumber, clampNumberNormalizer.apply(parsedMin), clampNumberNormalizer.apply(parsedMax));
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
