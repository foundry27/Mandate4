package pw.stamina.mandate.execution.flag;

import java.util.List;

/**
 * @author Mark Johnson
 */
public class FlagGroup {

    private final String groupName;

    private final List<Flag> flagsInGroup;

    public FlagGroup(final String groupName, final List<Flag> flagsInGroup) {
        this.groupName = groupName;
        this.flagsInGroup = flagsInGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<Flag> getFlagsInGroup() {
        return flagsInGroup;
    }
}
