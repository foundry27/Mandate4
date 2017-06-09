package pw.stamina.mandate.execution;

import pw.stamina.mandate.execution.flag.Flag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author Mark Johnson
 */
public class SimpleCommandParameter implements CommandParameter {

    private final Class<?> rawType;

    private final Type[] typeParameters;

    private final Annotation[] annotations;

    private final Flag flag;

    private final boolean implicit;

    private final boolean varargs;

    public SimpleCommandParameter(final Class<?> rawType,
                                  final Type[] typeParameters,
                                  final Annotation[] annotations,
                                  final Flag flag,
                                  final boolean implicit, final boolean varargs) {
        this.rawType = rawType;
        this.typeParameters = typeParameters;
        this.annotations = annotations;
        this.flag = flag;
        this.implicit = implicit;
        this.varargs = varargs;
    }

    @Override
    public Class<?> getRawType() {
        return rawType;
    }

    @Override
    public Type[] getTypeParameters() {
        return typeParameters;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return (T) annotation;
            }
        }
        return null;
    }

    @Override
    public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Flag> findFlag() {
        return Optional.ofNullable(flag);
    }

    @Override
    public boolean isImplicit() {
        return implicit;
    }

    @Override
    public boolean isVarargs() {
        return varargs;
    }
}
