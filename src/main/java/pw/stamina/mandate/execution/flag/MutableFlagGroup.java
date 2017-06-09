package pw.stamina.mandate.execution.flag;

import java.util.List;

/**
 * @author Mark Johnson
 */
public final class MutableFlagGroup extends FlagGroup {
    public MutableFlagGroup(final String groupName, final List<Flag> flagsInGroup) {
        super(groupName, flagsInGroup);
    }

    public void addFlagToGroup(final Flag flag) {
        getFlagsInGroup().add(flag);
    }
}
