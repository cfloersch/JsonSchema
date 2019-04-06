package xpertss.json.schema.core.keyword.syntax.checkers.helpers;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Helper class to validate the syntax of all keywords taking a schema array as
 * a value
 *
 * <p>These keywords are, among others, {@code allOf}, {@code anyOf}, etc.</p>
 */
public final class SchemaArraySyntaxChecker
    extends AbstractSyntaxChecker
{
    public SchemaArraySyntaxChecker(final String keyword)
    {
        super(keyword, NodeType.ARRAY);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        final int size = getNode(tree).size();

        if (size == 0) {
            report.error(newMsg(tree, bundle, "common.array.empty"));
            return;
        }

        for (int index = 0; index < size; index++)
            pointers.add(JsonPointer.of(keyword, index));
    }
}
