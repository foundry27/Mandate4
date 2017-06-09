package pw.stamina.mandate.execution.flag;

import pw.stamina.mandate.annotations.flag.MixedFlag;
import pw.stamina.mandate.annotations.flag.Options;
import pw.stamina.mandate.annotations.flag.Requires;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mark Johnson
 */
public final class MixedFlagToFlagWithOptionalArgumentAdapter implements FlagWithOptionalArgument  {

    private final char[] shortOptions;

    private final String[] longOptions;

    private final MutableFlagGroup flagGroup;

    private final List<FlagGroup> incompatibleFlagGroups;

    private final List<FlagGroup> flagGroupDependencies;

    private final String tokenIfFlagPresentWithoutArg;

    private final String tokenIfFlagAbsent;

    public MixedFlagToFlagWithOptionalArgumentAdapter(final MixedFlag flag, final FlagGroupLookupService flagGroupLookupService) {
        final Options options = flag.value();
        this.shortOptions = options.shorter();
        this.longOptions = options.longer();
        this.flagGroup = !options.group().isEmpty() ? flagGroupLookupService.computeFlagGroupByNameIfAbsent(options.group()) : null;
        if (this.flagGroup != null) {
            this.flagGroup.addFlagToGroup(this);
        }
        final Requires requires = flag.req();
        this.incompatibleFlagGroups = Arrays.stream(requires.absent())
                .map(flagGroupLookupService::computeFlagGroupByNameIfAbsent)
                .collect(Collectors.toList());
        this.flagGroupDependencies = Arrays.stream(requires.present())
                .map(flagGroupLookupService::computeFlagGroupByNameIfAbsent)
                .collect(Collectors.toList());
        this.tokenIfFlagPresentWithoutArg = flag.noargdef().length > 0 ? flag.noargdef()[0] : null;
        this.tokenIfFlagAbsent = flag.elsedef().length > 0 ? flag.elsedef()[0] : null;
    }

    @Override
    public char[] getShortOptions() {
        return shortOptions;
    }

    @Override
    public String[] getLongOptions() {
        return longOptions;
    }

    @Override
    public Optional<FlagGroup> findFlagGroup() {
        return Optional.ofNullable(flagGroup);
    }

    @Override
    public List<FlagGroup> getIncompatibleGroups() {
        return incompatibleFlagGroups;
    }

    @Override
    public List<FlagGroup> getGroupDependencies() {
        return flagGroupDependencies;
    }

    @Override
    public String getTokenIfFlagPresentWithoutArgument() {
        return tokenIfFlagPresentWithoutArg;
    }

    @Override
    public String getTokenIfFlagAbsent() {
        return tokenIfFlagAbsent;
    }
}
