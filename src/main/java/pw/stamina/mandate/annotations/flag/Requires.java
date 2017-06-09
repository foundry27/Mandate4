package pw.stamina.mandate.annotations.flag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Mark Johnson
 */
@Retention(RetentionPolicy.CLASS)
public @interface Requires {
    String[] absent() default {};

    String[] present() default {};
}
