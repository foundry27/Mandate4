package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.execution.CommandParameter;

import java.util.List;

/**
 * @author Mark Johnson
 */
@FunctionalInterface
public interface CommandParameterExtractor<T> {
    List<CommandParameter> getCommandParameters(T t);
}
