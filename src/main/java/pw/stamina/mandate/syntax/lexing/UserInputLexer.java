package pw.stamina.mandate.syntax.lexing;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public interface UserInputLexer {
    Deque<String> tokenizeInput(String userInput) throws InputTokenizationException;
}
