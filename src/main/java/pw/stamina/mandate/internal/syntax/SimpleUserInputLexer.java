package pw.stamina.mandate.internal.syntax;

import pw.stamina.mandate.syntax.lexing.InputTokenizationException;
import pw.stamina.mandate.syntax.lexing.UserInputLexer;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Mark Johnson
 */
public class SimpleUserInputLexer implements UserInputLexer {
    @Override
    public Deque<String> tokenizeInput(final String userInput) throws InputTokenizationException {
        final Deque<String> arguments = new ArrayDeque<>();

        final StringBuilder content = new StringBuilder();
        boolean escaped = false, quoted = false; int depth = 0;
        for (final char character : userInput.toCharArray()) {
            if (escaped) {
                content.append(character);
                escaped = false;
            } else {
                switch (character) {
                    case '\\':
                        escaped = true;
                        break;
                    case '"':
                        quoted = !quoted;
                        if (depth > 0) {
                            content.append(character);
                        }
                        break;
                    case ']':
                        if (!quoted) {
                            depth--;
                        }
                        content.append(character);
                        break;
                    case '[':
                        if (!quoted) {
                            depth++;
                        }
                        content.append(character);
                        break;
                    case ' ':
                        if (!quoted && depth == 0) {
                            if (content.length() > 0) {
                                arguments.add(content.toString());
                                content.setLength(0);
                            }
                            break;
                        }
                    default:
                        content.append(character);
                }
            }
        }
        if (depth > 0) {
            throw new InputTokenizationException("Found unterminated list in input: missing " + depth + " terminators");
        } else if (depth < 0) {
            throw new InputTokenizationException("Found " + Math.abs(depth) + " too many list terminators in input");
        }

        if (content.length() > 0) {
            arguments.add(content.toString());
            content.setLength(0);
        }
        return arguments;
    }

}
