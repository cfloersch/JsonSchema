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
 * Syntax checker for the {@code exclusiveMaximum} keyword
 */
public final class ExclusiveMaximumSyntaxChecker
    extends AbstractSyntaxChecker
{
    private static final SyntaxChecker INSTANCE
        = new ExclusiveMaximumSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private ExclusiveMaximumSyntaxChecker()
    {
        super("exclusiveMaximum", NodeType.BOOLEAN);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        if (!tree.getNode().has("maximum"))
            report.error(newMsg(tree, bundle, "common.exclusiveMaximum"));
    }
}
