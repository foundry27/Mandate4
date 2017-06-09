package pw.stamina.mandate.execution;

import pw.stamina.mandate.annotations.Implicit;
import pw.stamina.mandate.annotations.flag.AutoFlag;
import pw.stamina.mandate.annotations.flag.MixedFlag;
import pw.stamina.mandate.annotations.flag.UserFlag;
import pw.stamina.mandate.execution.flag.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Mark Johnson
 */
public final class ReflectionParameterToCommandParameterAdapter implements CommandParameter {

    private final Class<?> rawType;

    private final Type[] typeParameters;

    private final Annotation[] annotations;

    private final Flag flag;

    private final boolean implicit;

    private final boolean varargs;

    public ReflectionParameterToCommandParameterAdapter(final Parameter parameter, final FlagGroupLookupService flagGroupLookupService) {
        this.rawType = parameter.getType();
        final Type parameterizedTypeLookup = parameter.getParameterizedType();
        if (parameterizedTypeLookup instanceof ParameterizedType) {
            this.typeParameters = ((ParameterizedType) parameterizedTypeLookup).getActualTypeArguments();
        } else {
            this.typeParameters = new Type[0];
        }
        this.annotations = parameter.getAnnotations();
        this.flag = getFlagReturningNullIfAbsent(parameter, flagGroupLookupService);
        this.implicit = parameter.isAnnotationPresent(Implicit.class);
        this.varargs = parameter.isVarArgs();
    }

    private static Flag getFlagReturningNullIfAbsent(final Parameter parameter, final FlagGroupLookupService flagGroupLookupService) {
        if (parameter.isAnnotationPresent(AutoFlag.class)) {
            return new AutoFlagToFlagWithNoArgumentAdapter(parameter.getDeclaredAnnotation(AutoFlag.class), flagGroupLookupService);
        } else if (parameter.isAnnotationPresent(UserFlag.class)) {
            return new UserFlagToFlagWithMandatoryArgumentAdapter(parameter.getDeclaredAnnotation(UserFlag.class), flagGroupLookupService);
        } else if (parameter.isAnnotationPresent(MixedFlag.class)) {
            return new MixedFlagToFlagWithOptionalArgumentAdapter(parameter.getDeclaredAnnotation(MixedFlag.class), flagGroupLookupService);
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getRawType() {
        return rawType;
    }

    @Override
    public Type[] getTypeParameters() {
        return Arrays.copyOf(typeParameters, typeParameters.length);
    }

    @Override
    public Annotation[] getAnnotations() {
        return Arrays.copyOf(annotations, annotations.length);
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
