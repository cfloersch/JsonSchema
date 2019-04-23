package xpertss.json.schema.core.keyword.syntax.checkers.draftv6;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.msgsimple.bundle.MessageBundle;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;

import java.util.Collection;

/**
 * Syntax checker for the {@code const} keyword
 */
public final class ConstSyntaxChecker extends AbstractSyntaxChecker {


    private static final SyntaxChecker INSTANCE = new ConstSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private ConstSyntaxChecker()
    {
        super("const", NodeType.STRING);
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle, ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        // TODO There is no check for value??
        /*
        Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        for (final JsonNode element: getNode(tree))
            if (!set.add(EQUIVALENCE.wrap(element))) {
                report.error(newMsg(tree, bundle,
                    "common.array.duplicateElements"));
                return;
            }
            */
    }
}
