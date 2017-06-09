package pw.stamina.mandate.execution.flag;

import java.util.List;
import java.util.Optional;

/**
 * @author Mark Johnson
 */
public abstract class AbstractFlag implements Flag {

    private final char[] shortOptions;

    private final String[] longOptions;

    private final FlagGroup flagGroup;

    private final List<FlagGroup> incompatibleFlagGroups;

    private final List<FlagGroup> flagGroupDependencies;

    protected AbstractFlag(final char[] shortOptions, final String[] longOptions, final FlagGroup flagGroup,
                           final List<FlagGroup> incompatibleFlagGroups, final List<FlagGroup> flagGroupDependencies) {
        this.shortOptions = shortOptions;
        this.longOptions = longOptions;
        this.flagGroup = flagGroup;
        this.incompatibleFlagGroups = incompatibleFlagGroups;
        this.flagGroupDependencies = flagGroupDependencies;
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
}
