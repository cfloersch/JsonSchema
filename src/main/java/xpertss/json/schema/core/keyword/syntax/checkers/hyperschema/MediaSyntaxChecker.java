package xpertss.json.schema.core.keyword.syntax.checkers.hyperschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.MediaType;

import java.util.Collection;
import java.util.Set;

/**
 * Syntax checker for draft v4 hyperschema's {@code media} keyword
 */
public final class MediaSyntaxChecker extends AbstractSyntaxChecker {

    private static final String BINARY_ENCODING_FIELDNAME = "binaryEncoding";
    private static final String TYPE_FIELDNAME = "type";

    // FIXME: INCOMPLETE: excludes "x-token" and "ietf-token"
    private static final Set<String> BINARY_ENCODINGS = ImmutableSet.of(
        "7bit", "8bit", "binary", "quoted-printable", "base64"
    );

    private static final SyntaxChecker INSTANCE = new MediaSyntaxChecker();

    private MediaSyntaxChecker()
    {
        super("media", NodeType.OBJECT);
    }

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = getNode(tree);
        JsonNode subNode;
        NodeType type;
        String value;

        subNode = node.path(BINARY_ENCODING_FIELDNAME);
        if (!subNode.isMissingNode()) {
            type = NodeType.getNodeType(subNode);
            value = subNode.textValue();
            if (value == null)
                report.error(newMsg(tree, bundle,
                    "draftv4.media.binaryEncoding.incorrectType")
                    .put("expected", NodeType.STRING)
                    .putArgument("found", type));
            else if (!BINARY_ENCODINGS.contains(value.toLowerCase()))
                report.error(newMsg(tree, bundle,
                    "draftv4.media.binaryEncoding.invalid")
                    .putArgument("value", value)
                    .putArgument("valid", BINARY_ENCODINGS));
        }

        subNode = node.path(TYPE_FIELDNAME);
        if (subNode.isMissingNode())
            return;
        type = NodeType.getNodeType(subNode);
        if (type != NodeType.STRING) {
            report.error(newMsg(tree, bundle,
                "draftv4.media.type.incorrectType")
                .put("expected", NodeType.STRING).putArgument("found", type));
            return;
        }
        value = subNode.textValue();
        try {
            MediaType.parse(value);
        } catch (IllegalArgumentException ignored) {
            report.error(newMsg(tree, bundle, "draftv4.media.type.notMediaType")
                .putArgument("value", value));
        }
    }
}
