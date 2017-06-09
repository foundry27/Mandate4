package pw.stamina.mandate.security;

/**
 * @author Mark Johnson
 */
public interface SystemUser {
    boolean hasPermission(Permission permission);

    String getName();
}
