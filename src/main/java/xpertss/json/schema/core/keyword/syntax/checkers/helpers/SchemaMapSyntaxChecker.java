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
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

/**
 * Helper class for keywords having an object as a value whose values are
 * schemas
 */
public abstract class SchemaMapSyntaxChecker
    extends AbstractSyntaxChecker
{
    protected SchemaMapSyntaxChecker(final String keyword)
    {
        super(keyword, NodeType.OBJECT);
    }

    @Override
    protected final void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        collectPointers(pointers, getNode(tree));
        extraChecks(report, bundle, tree);
    }

    /**
     * Perform extra checks on the value
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
        final JsonNode node)
    {
        // We know this is an object, so...
        final Set<String> set = Sets.newHashSet(node.fieldNames());
        for (final String s: Ordering.natural().sortedCopy(set))
            pointers.add(JsonPointer.of(keyword, s));
    }
}
