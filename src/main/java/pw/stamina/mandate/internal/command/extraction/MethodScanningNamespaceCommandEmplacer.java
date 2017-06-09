package pw.stamina.mandate.internal.command.extraction;

import pw.stamina.mandate.annotations.Executes;
import pw.stamina.mandate.execution.CommandParameter;
import pw.stamina.mandate.execution.executable.CommandExecutable;
import pw.stamina.mandate.execution.executable.CommandExecutionException;
import pw.stamina.mandate.execution.namespace.CommandNamespace;
import pw.stamina.mandate.execution.namespace.MutableCommandNamespace;
import pw.stamina.mandate.execution.result.ExitCode;
import pw.stamina.mandate.internal.command.execution.executable.DeferringCommandExecutable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Mark Johnson
 */
public final class MethodScanningNamespaceCommandEmplacer implements NamespaceCommandEmplacer<Object> {

    private final CommandParameterExtractor<Method> commandParameterExtractor;

    private final CommandNamespaceExtractor<Method> commandNamespaceExtractor;

    public MethodScanningNamespaceCommandEmplacer(final CommandParameterExtractor<Method> commandParameterExtractor,
                                                  final CommandNamespaceExtractor<Method> commandNamespaceExtractor) {
        this.commandParameterExtractor = commandParameterExtractor;
        this.commandNamespaceExtractor = commandNamespaceExtractor;
    }

    @Override
    public void emplaceCommandsInNamespace(final Object parent, final MutableCommandNamespace namespace) {
        getMethodsInClassHierarchy(parent)
                .filter(MethodScanningNamespaceCommandEmplacer::isValidExecutableMethod)
                .forEach(m -> {
                    final MutableCommandNamespace newNamespace = (MutableCommandNamespace) commandNamespaceExtractor.extractNamespace(m);
                    newNamespace.addCommand(createExecutableGenerator(parent, m).apply(newNamespace));
                });
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

    private Function<CommandNamespace, CommandExecutable> createExecutableGenerator(final Object parent, final Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return namespace -> createStaticMethodCommandExecutable(method, namespace, commandParameterExtractor.getCommandParameters(method));
        } else {
            return namespace -> createVirtualMethodCommandExecutable(method, parent, namespace, commandParameterExtractor.getCommandParameters(method));
        }
    }

    private static CommandExecutable createStaticMethodCommandExecutable(final Method method, final CommandNamespace namespace, final List<CommandParameter> commandParameters) {
        return new DeferringCommandExecutable(method.getDeclaredAnnotation(Executes.class).value(), namespace, commandParameters, args -> {
            try {
                return (ExitCode) method.invoke(null, args.getArguments().toArray());
            } catch (final ReflectiveOperationException e) {
                throw new CommandExecutionException(e);
            }
        });
    }

    private static CommandExecutable createVirtualMethodCommandExecutable(final Method method, final Object parent, final CommandNamespace namespace, final List<CommandParameter> commandParameters) {
        return new DeferringCommandExecutable(method.getDeclaredAnnotation(Executes.class).value(), namespace, commandParameters, args -> {
            try {
                return (ExitCode) method.invoke(parent, args.getArguments().toArray());
            } catch (final ReflectiveOperationException e) {
                throw new CommandExecutionException(e);
            }
        });
    }

}
