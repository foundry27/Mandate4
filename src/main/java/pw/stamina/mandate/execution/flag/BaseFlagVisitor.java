package pw.stamina.mandate.execution.flag;

/**
 * @author Mark Johnson
 */
public abstract class BaseFlagVisitor implements FlagVisitor {
    @Override
    public void visitFlagWithNoArg(final FlagWithNoArgument flagWithNoArgument) {}

    @Override
    public void visitFlagWithArg(final FlagWithMandatoryArgument flagWithMandatoryArgument) {}

    @Override
    public void visitFlagWithPossibleArg(final FlagWithOptionalArgument flagWithOptionalArgument) {}
}
