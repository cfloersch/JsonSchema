package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaOrSchemaArraySyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Syntax checker for draft v3's {@code items} keyword
 */
public final class DraftV3ItemsSyntaxChecker
    extends SchemaOrSchemaArraySyntaxChecker
{
    private static final SyntaxChecker INSTANCE
        = new DraftV3ItemsSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DraftV3ItemsSyntaxChecker()
    {
        super("items");
    }

    @Override
    protected void extraChecks(final ProcessingReport report,
        final MessageBundle bundle, final SchemaTree tree)
        throws ProcessingException
    {
    }
}
