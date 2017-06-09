package pw.stamina.mandate.execution;

import pw.stamina.mandate.execution.flag.Flag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author Mark Johnson
 */
public interface CommandParameter {
    Class<?> getRawType();

    Type[] getTypeParameters();

    Annotation[] getAnnotations();

    <T extends Annotation> T getAnnotation(Class<T> annotationType);

    boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

    Optional<Flag> findFlag();

    default boolean isImplicit() {
        return false;
    }

    default boolean isVarargs() {
        return false;
    }

    default String getTypeName() {
        return toString();
    }
}
