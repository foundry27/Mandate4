package pw.stamina.mandate.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mark Johnson
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RealClamp {
    double min() default Double.MIN_VALUE;

    double max() default Double.MAX_VALUE;

    ClampType type() default ClampType.COERCE;
}
