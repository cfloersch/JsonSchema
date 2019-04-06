package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.util.RegexECMA262Helper;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Syntax checker for the {@code pattern} keyword
 *
 * @see RegexECMA262Helper
 */
public final class PatternSyntaxChecker
    extends AbstractSyntaxChecker
{
    private static final SyntaxChecker INSTANCE = new PatternSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private PatternSyntaxChecker()
    {
        super("pattern", NodeType.STRING);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        final String value = getNode(tree).textValue();

        if (!RegexECMA262Helper.regexIsValid(value))
            report.error(newMsg(tree, bundle, "common.invalidRegex")
                .putArgument("value", value));
    }
}
