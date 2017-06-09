package pw.stamina.mandate.io.builders.input;

import java.util.concurrent.TimeUnit;

/**
 * @author Mark Johnson
 */
public interface InputBuilderLike<
        RAW_BUILDER extends InputBuilderLike<?, ?, ?>,
        CHAR_LIMITED_BUILDER extends InputBuilderLike<?, ?, ?>,
        TIME_LIMITED_BUILDER extends InputBuilderLike<?, ?, ?>
        > {
    RAW_BUILDER asRaw();

    CHAR_LIMITED_BUILDER withCharLimit(int limit);

    TIME_LIMITED_BUILDER withTimeout(long timeout, TimeUnit unit);
}
