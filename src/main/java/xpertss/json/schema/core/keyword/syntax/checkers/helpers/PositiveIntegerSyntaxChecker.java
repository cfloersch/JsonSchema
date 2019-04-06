package xpertss.json.schema.core.keyword.syntax.checkers.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Helper class to check the syntax of all keywords having a positive integer
 * as a valid value
 */
public final class PositiveIntegerSyntaxChecker
    extends AbstractSyntaxChecker
{
    public PositiveIntegerSyntaxChecker(final String keyword)
    {
        super(keyword, NodeType.INTEGER);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = getNode(tree);

        if (!node.canConvertToInt()) {
            report.error(newMsg(tree, bundle, "common.integerTooLarge")
                .put("max", Integer.MAX_VALUE));
            return;
        }

        if (node.intValue() < 0)
            report.error(newMsg(tree, bundle, "common.integerIsNegative"));
    }
}
