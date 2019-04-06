package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNumEquals;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 * Syntax checker for draft v4's {@code required} keyword
 */
public final class RequiredSyntaxChecker
    extends AbstractSyntaxChecker
{
    private static final Equivalence<JsonNode> EQUIVALENCE
        = JsonNumEquals.getInstance();

    private static final SyntaxChecker INSTANCE = new RequiredSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private RequiredSyntaxChecker()
    {
        super("required", NodeType.ARRAY);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = getNode(tree);
        final int size = node.size();

        if (size == 0) {
            report.error(newMsg(tree, bundle, "common.array.empty"));
            return;
        }

        final Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        boolean uniqueElements = true;
        JsonNode element;
        NodeType type;

        for (int index = 0; index < size; index++) {
            element = node.get(index);
            uniqueElements = set.add(EQUIVALENCE.wrap(element));
            type = NodeType.getNodeType(element);
            if (type != NodeType.STRING)
                report.error(newMsg(tree, bundle,
                    "common.array.element.incorrectType")
                    .putArgument("index", index)
                    .putArgument("expected", EnumSet.of(NodeType.STRING))
                    .putArgument("found", type)
                );
        }

        if (!uniqueElements)
            report.error(newMsg(tree, bundle, "common.array.duplicateElements"));
    }
}
