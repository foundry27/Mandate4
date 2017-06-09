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
public @interface PreciseClamp {
    String min() default "";

    String max() default "";

    ClampType type() default ClampType.COERCE;
}
