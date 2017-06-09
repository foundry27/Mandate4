package pw.stamina.mandate.internal.command.location;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public interface CommandLocator {
    CommandLookup locateCommandMatchingInput(Deque<String> tokens);
}
