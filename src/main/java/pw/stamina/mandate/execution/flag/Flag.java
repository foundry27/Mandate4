package pw.stamina.mandate.execution.flag;

import java.util.List;
import java.util.Optional;

/**
 * @author Mark Johnson
 */
public interface Flag {
    char[] getShortOptions();

    String[] getLongOptions();

    Optional<FlagGroup> findFlagGroup();

    List<FlagGroup> getIncompatibleGroups();

    List<FlagGroup> getGroupDependencies();


}
