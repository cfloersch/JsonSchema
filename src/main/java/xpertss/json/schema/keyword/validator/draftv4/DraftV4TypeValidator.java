package xpertss.json.schema.keyword.validator.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.EnumSet;

/**
 * Keyword validator for draft v4's {@code type}
 */
public final class DraftV4TypeValidator extends AbstractKeywordValidator {

    private final EnumSet<NodeType> types = EnumSet.noneOf(NodeType.class);

    public DraftV4TypeValidator(JsonNode digest)
    {
        super("type");
        for (final JsonNode node: digest.get(keyword))
            types.add(NodeType.fromName(node.textValue()));
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        NodeType type = NodeType.getNodeType(data.getInstance().getNode());

        if (!types.contains(type))
            report.error(newMsg(data, bundle, "err.common.typeNoMatch")
                .putArgument("found", type)
                .putArgument("expected", toArrayNode(types)));
    }

    @Override
    public String toString()
    {
        return keyword + ": " + types;
    }
}
