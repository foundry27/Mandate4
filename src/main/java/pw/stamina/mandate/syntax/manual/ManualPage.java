package pw.stamina.mandate.syntax.manual;

import java.util.List;
import java.util.Locale;

/**
 * @author Mark Johnson
 */
public interface ManualPage {
    List<ManualSection> getManualSections(Locale locale);
}
