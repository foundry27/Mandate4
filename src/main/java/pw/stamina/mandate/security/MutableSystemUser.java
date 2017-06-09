package pw.stamina.mandate.security;

/**
 * @author Mark Johnson
 */
public interface MutableSystemUser extends SystemUser {
    void addPermission(Permission permission);

    void removePermission(Permission permission);
}
