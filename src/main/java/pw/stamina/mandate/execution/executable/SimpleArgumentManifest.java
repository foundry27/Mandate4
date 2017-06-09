package pw.stamina.mandate.execution.executable;

import java.util.Collections;
import java.util.List;

/**
 * @author Mark Johnson
 */
public final class SimpleArgumentManifest implements ArgumentManifest {

    private final List<Object> arguments;

    public SimpleArgumentManifest(final List<Object> arguments) {
        this.arguments = Collections.unmodifiableList(arguments);
    }

    @Override
    public List<Object> getArguments() {
        return arguments;
    }
}
