package xpertss.json.schema.core.keyword.syntax.checkers.helpers;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * Helper class to validate the syntax of all keywords whose value must be a URI
 *
 * <p>Note that this checker checks that URIs are normalized.</p>
 */
public final class URISyntaxChecker extends AbstractSyntaxChecker {

    public URISyntaxChecker(String keyword)
    {
        super(keyword, NodeType.STRING);
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        String s = getNode(tree).textValue();

        try {
            URI uri = new URI(s);
            if (!uri.equals(uri.normalize()))
                report.error(newMsg(tree, bundle, "common.uri.notNormalized")
                    .putArgument("value", s));
        } catch (URISyntaxException ignored) {
            report.error(newMsg(tree, bundle, "common.uri.invalid")
                .putArgument("value", s));
        }
    }
}
