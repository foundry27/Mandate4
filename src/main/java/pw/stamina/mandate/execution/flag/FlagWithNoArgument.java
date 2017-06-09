package pw.stamina.mandate.execution.flag;

/**
 * @author Mark Johnson
 */
public interface FlagWithNoArgument extends Flag {
    String getTokenIfFlagPresent();

    String getTokenIfFlagAbsent();
}
