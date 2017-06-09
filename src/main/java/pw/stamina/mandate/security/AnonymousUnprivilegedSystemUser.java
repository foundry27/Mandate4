package pw.stamina.mandate.security;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mark Johnson
 */
public final class AnonymousUnprivilegedSystemUser implements SystemUser {

    private static final AtomicLong USER_ID_COUNTER = new AtomicLong();

    private final long identifier;

    public AnonymousUnprivilegedSystemUser() {
        this.identifier = USER_ID_COUNTER.getAndIncrement();
    }

    @Override
    public boolean hasPermission(final Permission permission) {
        return false;
    }

    @Override
    public String getName() {
        return "anonymous$" + identifier;
    }

}
