package pw.stamina.mandate.io.builders.output;

/**
 * @author Mark Johnson
 */
public interface OutputBuilderLike<T extends OutputBuilderLike<?>> {
    T withLineEnding(String lineEnding);
}
