package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaOrSchemaArraySyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Syntax checker for draft v3's {@code extends} keyword
 */
public final class ExtendsSyntaxChecker extends SchemaOrSchemaArraySyntaxChecker {

    private static final SyntaxChecker INSTANCE = new ExtendsSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private ExtendsSyntaxChecker()
    {
        super("extends");
    }

    @Override
    protected void extraChecks(ProcessingReport report, MessageBundle bundle, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = tree.getNode().get(keyword);
        if (node.isArray() && node.size() == 0)
            report.warn(newMsg(tree, bundle, "draftv3.extends.emptyArray"));
    }
}
