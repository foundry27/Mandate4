package pw.stamina.mandate.annotations.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author Mark Johnson
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Match {
    String value();

    MatchType type() default MatchType.STRICT_EQUALITY;
}