package pw.stamina.mandate.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mark Johnson
 */
public final class Permission {

    public static final Permission WILDCARD = new Permission("*");

    private static final Map<String, Permission> PERMISSION_CACHE = new HashMap<>();

    private final String permission;

    private Permission(final String permission) {
        this.permission = permission;
    }

    public String getRawName() {
        return permission;
    }

    public static Permission of(final String permission) {
        return PERMISSION_CACHE.computeIfAbsent(permission, Permission::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Permission that = (Permission) o;
        return Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permission='" + permission + '\'' +
                '}';
    }
}
