package xpertss.json.schema.core.keyword.syntax.checkers.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.InvalidSchemaException;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Helper class to validate the syntax of keywords having either a schema or
 * schema array as a value
 */
public abstract class SchemaOrSchemaArraySyntaxChecker
    extends AbstractSyntaxChecker
{
    protected SchemaOrSchemaArraySyntaxChecker(final String keyword)
    {
        super(keyword, NodeType.ARRAY, NodeType.OBJECT);
    }

    @Override
    protected final void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        collectPointers(pointers, tree);
        extraChecks(report, bundle, tree);
    }

    /**
     * Perform extra check on the keyword
     *
     * @param report the report to use
     * @param bundle the message bundle to use
     * @param tree the schema
     * @throws InvalidSchemaException schema is invalid
     */
    protected abstract void extraChecks(final ProcessingReport report,
        final MessageBundle bundle, final SchemaTree tree)
        throws ProcessingException;

    private void collectPointers(final Collection<JsonPointer> pointers,
        final SchemaTree tree)
    {
        final JsonNode node = getNode(tree);
        if (node.isObject()) {
            pointers.add(JsonPointer.of(keyword));
            return;
        }

        for (int index = 0; index < node.size(); index++)
            pointers.add(JsonPointer.of(keyword, index));
    }
}
