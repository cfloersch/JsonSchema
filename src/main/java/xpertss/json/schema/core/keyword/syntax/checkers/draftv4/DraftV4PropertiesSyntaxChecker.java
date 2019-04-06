package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaMapSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Syntax checker for draft v4's {@code properties} keyword
 */
public final class DraftV4PropertiesSyntaxChecker
    extends SchemaMapSyntaxChecker
{
    private static final SyntaxChecker INSTANCE
        = new DraftV4PropertiesSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DraftV4PropertiesSyntaxChecker()
    {
        super("properties");
    }
    @Override
    protected void extraChecks(final ProcessingReport report,
        final MessageBundle bundle, final SchemaTree tree)
        throws ProcessingException
    {
    }
}
