package xpertss.json.schema.core.keyword.syntax.checkers.common;

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
import java.util.Set;

/**
 * Syntax checker for the {@code enum} keyword
 *
 * @see JsonNumEquals
 */
public final class EnumSyntaxChecker extends AbstractSyntaxChecker {

    private static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();

    private static final SyntaxChecker INSTANCE = new EnumSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private EnumSyntaxChecker()
    {
        super("enum", NodeType.ARRAY);
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        for (final JsonNode element: getNode(tree))
            if (!set.add(EQUIVALENCE.wrap(element))) {
                report.error(newMsg(tree, bundle,
                    "common.array.duplicateElements"));
                return;
            }
    }
}
