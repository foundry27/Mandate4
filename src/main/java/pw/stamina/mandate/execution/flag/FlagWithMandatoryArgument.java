package pw.stamina.mandate.execution.flag;

/**
 * @author Mark Johnson
 */
public interface FlagWithMandatoryArgument extends Flag {
    String getTokenIfFlagAbsent();
}
