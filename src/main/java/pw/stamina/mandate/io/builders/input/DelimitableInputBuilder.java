package pw.stamina.mandate.io.builders.input;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Mark Johnson
 */
public interface DelimitableInputBuilder<T, DELIMITED_T> extends Supplier<T>,
        InputBuilderLike<DelimitableInputBuilder<T, DELIMITED_T>, DelimitableInputBuilder<T, DELIMITED_T>, DelimitableInputBuilder<Optional<T>, DELIMITED_T>> {
    @Override
    T get();

    BaseInputBuilder<DELIMITED_T> delimitingBy(String delimiter);
}
