package pw.stamina.mandate.internal;

import pw.stamina.mandate.CommandPath;
import pw.stamina.mandate.CommandShell;
import pw.stamina.mandate.SystemSession;
import pw.stamina.mandate.environment.SessionEnvironment;
import pw.stamina.mandate.environment.SimpleSessionEnvironment;
import pw.stamina.mandate.environment.implicit.DeferringImplicitValueProvider;
import pw.stamina.mandate.environment.implicit.ImplicitValueProvider;
import pw.stamina.mandate.execution.SimpleCommandNamespace;
import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;
import pw.stamina.mandate.internal.argument.SimpleArgumentParserLookupService;
import pw.stamina.mandate.internal.argument.SimpleCommandArgumentCollectorService;
import pw.stamina.mandate.internal.argument.SimpleExplicitArgumentResolutionService;
import pw.stamina.mandate.internal.argument.implicit.SimpleImplicitValueLookupService;
import pw.stamina.mandate.internal.command.execution.*;
import pw.stamina.mandate.internal.command.extraction.MethodCommandNamespaceExtractor;
import pw.stamina.mandate.internal.command.extraction.MethodCommandParameterExtractor;
import pw.stamina.mandate.internal.command.extraction.MethodScanningNamespaceCommandEmplacer;
import pw.stamina.mandate.internal.command.extraction.MethodScanningNamespaceCommandRemover;
import pw.stamina.mandate.internal.command.location.NamespaceScanningCommandLocator;
import pw.stamina.mandate.internal.argument.parsers.array.LengthLimitingArrayArgumentParserDecorator;
import pw.stamina.mandate.internal.argument.parsers.array.UniversalArrayArgumentParser;
import pw.stamina.mandate.internal.argument.parsers.bool.PrimitiveTypeWrappingBooleanArgumentParserDecorator;
import pw.stamina.mandate.internal.argument.parsers.bool.SimpleBooleanArgumentParser;
import pw.stamina.mandate.internal.argument.parsers.enumeration.SimpleEnumArgumentParser;
import pw.stamina.mandate.internal.argument.parsers.number.*;
import pw.stamina.mandate.internal.argument.parsers.text.character.PatternMatchingCharacterArgumentParserDecorator;
import pw.stamina.mandate.internal.argument.parsers.text.character.PrimitiveTypeWrappingCharacterArgumentParserDecorator;
import pw.stamina.mandate.internal.argument.parsers.text.character.SimpleCharacterArgumentParser;
import pw.stamina.mandate.internal.argument.parsers.text.string.LengthLimitingCharSequenceArgumentParserDecorator;
import pw.stamina.mandate.internal.argument.parsers.text.string.PatternMatchingCharSequenceArgumentParserDecorator;
import pw.stamina.mandate.internal.argument.parsers.text.string.SimpleCharSequenceArgumentParser;
import pw.stamina.mandate.internal.syntax.SimpleUserInputLexer;
import pw.stamina.mandate.io.StandardIO;
import pw.stamina.mandate.io.raw.RawIO;
import pw.stamina.mandate.security.SystemUser;
import pw.stamina.mandate.syntax.parsing.ArgumentParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Mark Johnson
 */
public final class SimpleSystemSession implements SystemSession {

    private final SystemUser user;

    private final MutableCommandNamespace globalNamespace;

    private final CommandPath commandPath;

    private final SessionEnvironment sessionEnvironment;

    public SimpleSystemSession(final SystemUser user) {
        this.user = user;
        this.globalNamespace = new SimpleCommandNamespace(null, "");
        this.commandPath = new SimpleCommandPath(
                new MethodScanningNamespaceCommandEmplacer(
                        MethodCommandParameterExtractor.INSTANCE,
                        new MethodCommandNamespaceExtractor(globalNamespace)),
                MethodScanningNamespaceCommandRemover.INSTANCE,
                globalNamespace);
        this.sessionEnvironment = new SimpleSessionEnvironment();
    }

    @Override
    public CommandPath getCommandPath() {
        return commandPath;
    }

    @Override
    public CommandShell makeShell(final RawIO io) {
        return new SimpleCommandShell(io,
                new SimpleCommandExecutor(user,
                        new SimpleCommandArgumentCollectorService(
                                new SimpleImplicitValueLookupService(
                                        Arrays.asList(createStandardIOProvider(io))
                                ),
                                new SimpleExplicitArgumentResolutionService(
                                        new SimpleArgumentParserLookupService(
                                                createDefaultArgumentParsers()
                                        ))), e -> StandardIO.from(io).err().write(
                                                "Encountered " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getLocalizedMessage())),
                new NamespaceScanningCommandLocator(globalNamespace),
                new SimpleUserInputLexer());
    }

    @Override
    public SystemUser getSessionUser() {
        return user;
    }

    @Override
    public SessionEnvironment getSessionEnvironment() {
        return sessionEnvironment;
    }

    private static ImplicitValueProvider createStandardIOProvider(final RawIO rawIO) {
        return new DeferringImplicitValueProvider(
                param -> param.getRawType() == StandardIO.class,
                param -> StandardIO.from(rawIO)
        );
    }

    private static Collection<ArgumentParser<?>> createDefaultArgumentParsers() {
        final List<ArgumentParser<?>> parsers = new ArrayList<>(5);
        parsers.add(new LengthLimitingCharSequenceArgumentParserDecorator(
                new PatternMatchingCharSequenceArgumentParserDecorator(
                        SimpleCharSequenceArgumentParser.INSTANCE)));
        parsers.add(new PrimitiveTypeWrappingNumberArgumentParserDecorator(
                new PreciseValueClampingNumberArgumentParserDecorator(
                        new RealValueClampingNumberArgumentParserDecorator(
                                new IntegerValueClampingNumberArgumentParserDecorator(
                                        SimpleNumberArgumentParser.INSTANCE)))));
        parsers.add(new PrimitiveTypeWrappingBooleanArgumentParserDecorator(
                SimpleBooleanArgumentParser.INSTANCE));
        parsers.add(new PrimitiveTypeWrappingCharacterArgumentParserDecorator(
                new PatternMatchingCharacterArgumentParserDecorator(
                        SimpleCharacterArgumentParser.INSTANCE)));
        parsers.add(SimpleEnumArgumentParser.INSTANCE);
        parsers.add(new LengthLimitingArrayArgumentParserDecorator(
                new UniversalArrayArgumentParser(parameter -> parsers.stream()
                        .filter(p -> p.canParseForParameter(parameter))
                        .findFirst().orElse(null))));
        return parsers;
    }
}
