package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Syntax checker for draft v4's {@code not} keyword
 */
public final class NotSyntaxChecker extends AbstractSyntaxChecker {

    private static final SyntaxChecker INSTANCE = new NotSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private NotSyntaxChecker()
    {
        super("not", NodeType.ARRAY, NodeType.values());
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        pointers.add(JsonPointer.of(keyword));
    }
}
