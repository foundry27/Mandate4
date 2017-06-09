package pw.stamina.mandate.io.builders.input;

import java.util.function.Supplier;

/**
 * @author Mark Johnson
 */
public interface BaseInputBuilder<T> extends Supplier<T>,
        InputBuilderLike<BaseInputBuilder<T>, BaseInputBuilder<T>, BaseInputBuilder<T>> {
    @Override
    T get();
}
