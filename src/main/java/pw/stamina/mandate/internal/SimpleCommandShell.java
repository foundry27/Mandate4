package pw.stamina.mandate.internal;

import pw.stamina.mandate.CommandShell;
import pw.stamina.mandate.execution.result.Execution;
import pw.stamina.mandate.execution.result.Executions;
import pw.stamina.mandate.execution.result.ExitCodes;
import pw.stamina.mandate.internal.command.execution.CommandExecutor;
import pw.stamina.mandate.internal.command.location.CommandLocator;
import pw.stamina.mandate.internal.command.location.CommandLookup;
import pw.stamina.mandate.io.StandardIO;
import pw.stamina.mandate.io.raw.RawIO;
import pw.stamina.mandate.syntax.lexing.UserInputLexer;

import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class SimpleCommandShell implements CommandShell {

    private final RawIO rawIO;

    private final CommandExecutor commandExecutor;

    private final CommandLocator commandLocator;

    private final UserInputLexer inputLexer;

    public SimpleCommandShell(final RawIO rawIO, final CommandExecutor commandExecutor,
                              final CommandLocator commandLocator, final UserInputLexer inputLexer) {
        this.rawIO = rawIO;
        this.commandExecutor = commandExecutor;
        this.commandLocator = commandLocator;
        this.inputLexer = inputLexer;
    }

    @Override
    public Execution execute(final String command) {
        final Deque<String> tokens = inputLexer.tokenizeInput(command);
        final CommandLookup commandLookup = commandLocator.locateCommandMatchingInput(tokens);
        if (commandLookup.wasSuccessful()) {
            return commandExecutor.execute(commandLookup.get(), tokens);
        } else {
            StandardIO.from(rawIO).err().write(commandLookup.failureMessage());
            return Executions.complete(ExitCodes.INVALID);
        }
    }
}
