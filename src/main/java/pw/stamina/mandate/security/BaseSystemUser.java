package pw.stamina.mandate.security;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Mark Johnson
 */
public class BaseSystemUser implements MutableSystemUser {

    private static final String PERMISSION_BLOCK_SEPARATOR = ".";

    private static final Pattern PERMISSION_BLOCK_SEPARATOR_SPLITTER = Pattern.compile(Pattern.quote(PERMISSION_BLOCK_SEPARATOR));

    private final String name;

    private final Set<Permission> permissions;

    public BaseSystemUser(final String name, final Set<Permission> permissions) {
        this.name = name;
        this.permissions = new HashSet<>(permissions);
    }

    @Override
    public boolean hasPermission(final Permission permission) {
        if (permissions.contains(permission)) {
            return true;
        }
        final List<String> lookupPermissionBlocks = Arrays.asList(PERMISSION_BLOCK_SEPARATOR_SPLITTER.split(permission.getRawName()));
        for (final Permission presentPermission : permissions) {
            final List<String> presentPermissionBlocks = Arrays.asList(PERMISSION_BLOCK_SEPARATOR_SPLITTER.split(presentPermission.getRawName()));
            if (arePermissionBlocksCompatible(lookupPermissionBlocks.iterator(), presentPermissionBlocks.iterator())) {
                return true;
            }
        }
        return false;
    }

    private static boolean arePermissionBlocksCompatible(final Iterator<String> lookupIterator, final Iterator<String> presentIterator) {
        while (lookupIterator.hasNext() && presentIterator.hasNext()) {
            final String lookupBlock = lookupIterator.next();
            final String presentBlock = presentIterator.next();

            if (!lookupBlock.equals(presentBlock) && !presentBlock.equals(Permission.WILDCARD.getRawName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addPermission(final Permission permission) {
        this.permissions.add(permission);
    }

    @Override
    public void removePermission(final Permission permission) {
        this.permissions.remove(permission);
    }
}
