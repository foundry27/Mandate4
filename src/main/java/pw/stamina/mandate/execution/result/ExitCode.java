package pw.stamina.mandate.execution.result;

/**
 * @author Mark Johnson
 */
public interface ExitCode {
    int getCode();

    default String getIdentifier() {
        return Integer.toString(getCode());
    }
}
