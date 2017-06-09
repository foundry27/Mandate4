package pw.stamina.mandate.annotations.scoping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Mark Johnson
 */
@Retention(RetentionPolicy.CLASS)
public @interface Namespace {
    String value();
}
