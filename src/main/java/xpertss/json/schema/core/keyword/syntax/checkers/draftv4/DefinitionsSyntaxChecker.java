package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaMapSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Syntax checker for draft v4's {@code definitions} keyword
 */
public final class DefinitionsSyntaxChecker extends SchemaMapSyntaxChecker {

    private static final SyntaxChecker INSTANCE = new DefinitionsSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DefinitionsSyntaxChecker()
    {
        super("definitions");
    }

    @Override
    protected void extraChecks(ProcessingReport report, MessageBundle bundle, SchemaTree tree)
        throws ProcessingException
    {
    }
}
