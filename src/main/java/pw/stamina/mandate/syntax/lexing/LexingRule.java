package pw.stamina.mandate.syntax.lexing;

import java.util.Optional;

/**
 * @author Mark Johnson
 */
public interface LexingRule {
    Optional<String[]> tryExtractingTokens(final String userInput);
}
