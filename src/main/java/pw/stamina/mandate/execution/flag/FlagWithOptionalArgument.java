package pw.stamina.mandate.execution.flag;

/**
 * @author Mark Johnson
 */
public interface FlagWithOptionalArgument extends Flag {
    String getTokenIfFlagPresentWithoutArgument();

    String getTokenIfFlagAbsent();
}
