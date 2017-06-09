package pw.stamina.mandate.execution.flag;

/**
 * @author Mark Johnson
 */
public interface FlagVisitor {
    void visitFlagWithNoArg(final FlagWithNoArgument flagWithNoArgument);

    void visitFlagWithArg(final FlagWithMandatoryArgument flagWithMandatoryArgument);

    void visitFlagWithPossibleArg(final FlagWithOptionalArgument flagWithOptionalArgument);
}
