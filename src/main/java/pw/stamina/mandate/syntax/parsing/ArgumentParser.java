package pw.stamina.mandate.syntax.parsing;

import io.vavr.control.Try;
import pw.stamina.mandate.execution.CommandParameter;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public interface ArgumentParser<T> {
    Try<T> parse(Deque<String> arguments, CommandParameter parameter);

    boolean canParseForParameter(CommandParameter parameter);
}
