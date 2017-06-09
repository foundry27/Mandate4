package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.annotations.scoping.Namespace;
import pw.stamina.mandate.annotations.scoping.ScopeBase;
import pw.stamina.mandate.annotations.scoping.Scoped;
import pw.stamina.mandate.execution.SimpleCommandNamespace;
import pw.stamina.mandate.execution.namespace.CommandNamespace;
import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Mark Johnson
 */
public final class MethodCommandNamespaceExtractor implements CommandNamespaceExtractor<Method> {

    private final MutableCommandNamespace globalNamespace;

    public MethodCommandNamespaceExtractor(final MutableCommandNamespace globalNamespace) {
        this.globalNamespace = globalNamespace;
    }

    @Override
    public CommandNamespace extractNamespace(final Method method) {
        final Deque<MutableCommandNamespace> namespaceStack = createNamespaceStackFromGlobalNamespace();
        if (!doesMethodOverrideLocalScope(method)) {
            fillNamespaceStackWithClassNamespacesIfPresent(namespaceStack, method.getDeclaringClass());
        }
        fillNamespaceStackWithMethodNamespacesIfPresent(namespaceStack, method);
        return namespaceStack.pop();
    }

    private Deque<MutableCommandNamespace> createNamespaceStackFromGlobalNamespace() {
        final Deque<MutableCommandNamespace> namespaceStack = new ArrayDeque<>();
        namespaceStack.push(globalNamespace);
        return namespaceStack;
    }

    private static boolean doesMethodOverrideLocalScope(final Method method) {
        final Scoped scopeAnnotation = method.getAnnotation(Scoped.class);
        return scopeAnnotation != null && scopeAnnotation.base() == ScopeBase.GLOBAL;
    }

    private static void fillNamespaceStackWithClassNamespacesIfPresent(final Deque<MutableCommandNamespace> namespaceStack, final Class<?> encapsulatingClass) {
        final Deque<Scoped> enclosingScopeStack = populateEnclosingClassScopesUpToRoot(encapsulatingClass);
        for (final Scoped anEnclosingScopeStack : enclosingScopeStack) {
            populateNamespaceStackWithNamespaces(namespaceStack, anEnclosingScopeStack.value());
        }
    }

    private static Deque<Scoped> populateEnclosingClassScopesUpToRoot(final Class<?> encapsulatingClass) {
        final Deque<Scoped> enclosingScopeStack = new ArrayDeque<>();
        for (Class<?> classScope = encapsulatingClass; classScope != null; classScope = classScope.getEnclosingClass()) {
            final Scoped scopeAnnotation = classScope.getAnnotation(Scoped.class);
            if (scopeAnnotation != null) {
                enclosingScopeStack.push(scopeAnnotation);
                if (scopeAnnotation.base() == ScopeBase.GLOBAL) break;
            }
        }
        return enclosingScopeStack;
    }

    private static void fillNamespaceStackWithMethodNamespacesIfPresent(final Deque<MutableCommandNamespace> namespaceStack, final Method method) {
        final Scoped scopeAnnotation = method.getAnnotation(Scoped.class);
        if (scopeAnnotation != null) {
            populateNamespaceStackWithNamespaces(namespaceStack, scopeAnnotation.value());
        }
    }

    private static void populateNamespaceStackWithNamespaces(final Deque<MutableCommandNamespace> namespaceStack, final Namespace[] namespaces) {
        for (final Namespace namespace : namespaces) {
            final MutableCommandNamespace parentNamespace = namespaceStack.peek();
            final MutableCommandNamespace existingNamespaceLookup = (MutableCommandNamespace) parentNamespace.getSubNamespace(namespace.value());
            if (existingNamespaceLookup != null) {
                namespaceStack.push(existingNamespaceLookup);
            } else {
                namespaceStack.push(new SimpleCommandNamespace(parentNamespace, namespace.value()));
            }
        }
    }
}
