package pw.stamina.mandate.annotations.flag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mark Johnson
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MixedFlag {
    Options value() default @Options;

    String[] noargdef() default {};

    String[] elsedef() default {};

    Requires req() default @Requires;
}
