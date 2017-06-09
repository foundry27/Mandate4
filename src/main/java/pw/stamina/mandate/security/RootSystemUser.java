package pw.stamina.mandate.security;

/**
 * @author Mark Johnson
 */
public enum RootSystemUser implements SystemUser {
    INSTANCE;

    @Override
    public boolean hasPermission(final Permission permission) {
        return true;
    }

    @Override
    public String getName() {
        return "root";
    }

}
