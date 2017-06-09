package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.annotations.Executes;
import pw.stamina.mandate.annotations.scoping.Namespace;
import pw.stamina.mandate.annotations.scoping.ScopeBase;
import pw.stamina.mandate.annotations.scoping.Scoped;
import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;
import pw.stamina.mandate.execution.result.ExitCode;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Mark Johnson
 */
public enum MethodScanningNamespaceCommandRemover implements NamespaceCommandRemover<Object> {
    INSTANCE;

    @Override
    public void removeCommandsInNamespace(final Object o, final MutableCommandNamespace namespace) {
        getMethodsInClassHierarchy(o)
                .filter(MethodScanningNamespaceCommandRemover::isValidExecutableMethod)
                .forEach(m -> removeMatchingExecutablesFromNamespace(m, namespace));
    }

    private static Stream<Method> getMethodsInClassHierarchy(final Object parent) {
        final Stream.Builder<Method> builder = Stream.builder();
        accumulateMethodsInClassHierarchyToBuilder(parent.getClass(), builder);
        return builder.build();
    }

    private static void accumulateMethodsInClassHierarchyToBuilder(final Class<?> clazz, final Stream.Builder<Method> builder) {
        for (final Method method : clazz.getDeclaredMethods()) {
            builder.accept(method);
        }
        for (final Class<?> innerClazz : clazz.getDeclaredClasses()) {
            accumulateMethodsInClassHierarchyToBuilder(innerClazz, builder);
        }
    }

    private static boolean isValidExecutableMethod(final Method method) {
        return method.isAnnotationPresent(Executes.class) && method.getReturnType() == ExitCode.class;
    }

    private static void removeMatchingExecutablesFromNamespace(final Method method, final MutableCommandNamespace globalNamespace) {
        final Executes commandData = method.getDeclaredAnnotation(Executes.class);

        final Deque<Scoped> enclosingScopeStack = populateEnclosingClassScopesUpToRoot(method.getDeclaringClass());

        final Deque<MutableCommandNamespace> namespaceStack = new ArrayDeque<>();
        namespaceStack.push(globalNamespace);
        for (final Iterator<Scoped> it = enclosingScopeStack.descendingIterator(); it.hasNext();) {
            for (final Namespace namespace : it.next().value()) {
                namespaceStack.push((MutableCommandNamespace) namespaceStack.peek().getSubNamespace(namespace.value()));
            }
        }

        namespaceStack.peek().removeCommandByName(commandData.value());
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
}
