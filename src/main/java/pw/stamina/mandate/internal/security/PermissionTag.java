package pw.stamina.mandate.internal.security;

import pw.stamina.mandate.security.Permission;

import java.util.Collections;
import java.util.List;

/**
 * @author Mark Johnson
 */
public final class PermissionTag {

    private final Permission expectedPermission;

    private final List<Permission> alternatePermissions;

    public PermissionTag(final Permission expectedPermission, final List<Permission> alternatePermissions) {
        this.expectedPermission = expectedPermission;
        this.alternatePermissions = Collections.unmodifiableList(alternatePermissions);
    }

    public Permission getExpectedPermission() {
        return expectedPermission;
    }

    public List<Permission> getAlternatePermissions() {
        return alternatePermissions;
    }
}
