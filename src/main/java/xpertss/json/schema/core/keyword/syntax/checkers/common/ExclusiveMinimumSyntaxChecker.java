package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Syntax checker for the {@code exclusiveMinimum} keyword
 */
public final class ExclusiveMinimumSyntaxChecker
    extends AbstractSyntaxChecker
{
    private static final SyntaxChecker INSTANCE
        = new ExclusiveMinimumSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private ExclusiveMinimumSyntaxChecker()
    {
        super("exclusiveMinimum", NodeType.BOOLEAN);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        if (!tree.getNode().has("minimum"))
            report.error(newMsg(tree, bundle, "common.exclusiveMinimum"));
    }
}
