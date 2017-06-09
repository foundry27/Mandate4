package pw.stamina.mandate.internal;

import pw.stamina.mandate.CommandPath;
import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;
import pw.stamina.mandate.internal.command.extraction.NamespaceCommandEmplacer;
import pw.stamina.mandate.internal.command.extraction.NamespaceCommandRemover;

/**
 * @author Mark Johnson
 */
public final class SimpleCommandPath implements CommandPath {

    private final NamespaceCommandEmplacer<? super Object> emplacer;

    private final NamespaceCommandRemover<? super Object> remover;

    private final MutableCommandNamespace globalNamespace;

    public SimpleCommandPath(final NamespaceCommandEmplacer<? super Object> emplacer,
                             final NamespaceCommandRemover<? super Object> remover,
                             final MutableCommandNamespace globalNamespace) {
        this.emplacer = emplacer;
        this.remover = remover;
        this.globalNamespace = globalNamespace;
    }

    @Override
    public void register(final Object container) {
        emplacer.emplaceCommandsInNamespace(container, globalNamespace);
    }

    @Override
    public void unregister(final Object container) {
        remover.removeCommandsInNamespace(container, globalNamespace);
    }

}
