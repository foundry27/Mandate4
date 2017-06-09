package pw.stamina.mandate.annotations.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Mark Johnson
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String value();

    String[] or() default {};
}