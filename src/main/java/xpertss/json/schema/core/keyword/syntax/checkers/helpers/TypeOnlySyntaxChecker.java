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
 * Helper class to validate the syntax of keywords only requiring that their
 * value be of certain types
 */
public final class TypeOnlySyntaxChecker extends AbstractSyntaxChecker {

    public TypeOnlySyntaxChecker(String keyword, NodeType first, NodeType... other)
    {
        super(keyword, first, other);
    }

    @Override
    public void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                            ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
    }
}
