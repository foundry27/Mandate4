package pw.stamina.mandate.execution.flag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Johnson
 */
public final class FlagGroupLookupService {

    private final Map<String, MutableFlagGroup> flagGroupMap;

    public FlagGroupLookupService() {
        this.flagGroupMap = new HashMap<>();
    }

    public MutableFlagGroup computeFlagGroupByNameIfAbsent(final String groupName) {
        return flagGroupMap.computeIfAbsent(groupName, n -> new MutableFlagGroup(n, new ArrayList<>()));
    }

    public void addFlagGroup(final MutableFlagGroup flagGroup) {
        flagGroupMap.put(flagGroup.getGroupName(), flagGroup);
    }
}
