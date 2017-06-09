package pw.stamina.mandate.io.builders.output;

/**
 * @author Mark Johnson
 */
public interface OutputBuilder<T> extends OutputBuilderLike<OutputBuilder<T>> {
    void write(T output);
}
