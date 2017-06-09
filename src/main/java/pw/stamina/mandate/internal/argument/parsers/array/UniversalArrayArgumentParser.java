package pw.stamina.mandate.internal.argument.parsers.array;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.SimpleCommandParameter;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;
import pw.stamina.mandate.syntax.parsing.IllegalInputException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * @author Mark Johnson
 */
public final class UniversalArrayArgumentParser implements ArgumentParser<Object> {

    private final Function<CommandParameter, ArgumentParser<?>> parserProvider;

    public UniversalArrayArgumentParser(final Function<CommandParameter, ArgumentParser<?>> parserProvider) {
        this.parserProvider = parserProvider;
    }

    @Override
    public Try<Object> parse(final Deque<String> arguments, final CommandParameter parameter) {
        final CommandParameter componentTypeParameter = wrapCommandParameterAsComponentType(parameter);
        final ArgumentParser<?> handlerLookup = parserProvider.apply(componentTypeParameter);
        if (handlerLookup != null) {
            final String input = arguments.pop();
            final List<String> arrayComponents = splitArrayComponentsToStrings(input);
            return Try.of(() -> createArrayFromSerializedComponents(componentTypeParameter, arrayComponents, handlerLookup));
        } else {
            return Try.failure(new IllegalInputException("No argument parse exists for array component type " + parameter.getRawType().getComponentType().getCanonicalName()));
        }
    }

    private static List<String> splitArrayComponentsToStrings(final String input) {
        final List<String> rawComponents = new ArrayList<>();
        final StringBuilder rawComponent = new StringBuilder(input.length());
        final char[] inputChars = input.toCharArray();

        boolean escaped = false, quoted = false; int depth = 0;
        for (int idx = 1; idx < inputChars.length - 1; idx++) {
            if (escaped) {
                rawComponent.append(inputChars[idx]);
                escaped = false;
            } else {
                switch (inputChars[idx]) {
                    case '\\': {
                        escaped = true;
                        break;
                    }
                    case '"': {
                        quoted = !quoted;
                        break;
                    }
                    case '}':   //fall through
                    case ']':
                        if (!quoted) {
                            depth--;
                        }
                        rawComponent.append(inputChars[idx]);
                        break;
                    case '{':    //fall through
                    case '[':
                        if (!quoted) {
                            depth++;
                        }
                        rawComponent.append(inputChars[idx]);
                        break;
                    case ',': {
                        if (!quoted && depth == 0) {
                            while (inputChars[idx+1] == ' ') {
                                idx++;
                            }
                            if (rawComponent.length() > 0) {
                                rawComponents.add(rawComponent.toString());
                                rawComponent.setLength(0);
                            }
                            break;
                        }
                    }
                    case ' ': {
                        if (!quoted && depth == 0) {
                            if (inputChars[idx - 1] != ' ' && inputChars[idx - 1] != ',') {
                                throw new IllegalInputException("Array element at position " + idx + " is separated by space, but not comma delimited");
                            } else {
                                idx++;
                                break;
                            }
                        }
                    }
                    default: {
                        rawComponent.append(inputChars[idx]);
                        escaped = false;
                    }
                }
            }

        }
        if (rawComponent.length() > 0) {
            rawComponents.add(rawComponent.toString());
            rawComponent.setLength(0);
        }
        return rawComponents;
    }

    private static Object createArrayFromSerializedComponents(final CommandParameter componentTypeParameter, final List<String> arrayComponents, final ArgumentParser<?> argumentParser) {
        final Object resultArray = Array.newInstance(componentTypeParameter.getRawType(), arrayComponents.size());
        for (int i = 0; i < arrayComponents.size(); i++) {
            Array.set(resultArray, i, argumentParser.parse(new ArrayDeque<>(Collections.singleton(arrayComponents.get(i))), componentTypeParameter));
        }
        return resultArray;
    }

    @Override
    public boolean canParseForParameter(final CommandParameter parameter) {
        return parameter.getRawType().isArray();
    }

    private static CommandParameter wrapCommandParameterAsComponentType(final CommandParameter parameter) {
        return new SimpleCommandParameter(
                parameter.getRawType().getComponentType(),
                parameter.getTypeParameters(),
                parameter.getAnnotations(),
                parameter.findFlag().orElse(null),
                parameter.isImplicit(),
                parameter.isVarargs());
    }
}
