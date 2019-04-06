package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaOrSchemaArraySyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Syntax checker for draft v4's {@code items} keyword
 */
public final class DraftV4ItemsSyntaxChecker
    extends SchemaOrSchemaArraySyntaxChecker
{
    private static final SyntaxChecker INSTANCE
        = new DraftV4ItemsSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DraftV4ItemsSyntaxChecker()
    {
        super("items");
    }

    @Override
    protected void extraChecks(final ProcessingReport report,
        final MessageBundle bundle, final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = getNode(tree);

        if (node.isArray() && node.size() == 0)
            report.error(newMsg(tree, bundle, "common.array.empty"));
    }
}
