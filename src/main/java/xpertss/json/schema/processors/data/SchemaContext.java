package xpertss.json.schema.processors.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.processors.digest.SchemaDigester;
import xpertss.json.schema.processors.validation.ValidationChain;

/**
 * Input for both a {@link SchemaDigester} and a {@link ValidationChain}
 *
 * <p>This is essentially a {@link FullData} which only retains the type of the
 * instance to validate instead of the full instance.</p>
 *
 * @see NodeType#getNodeType(JsonNode)
 */
public final class SchemaContext
    implements MessageProvider
{
    private final SchemaTree schema;
    private final NodeType instanceType;

    public SchemaContext(final FullData data)
    {
        schema = data.getSchema();
        final JsonTree tree = data.getInstance();
        instanceType = tree != null
            ? NodeType.getNodeType(tree.getNode())
            : null;
    }

    public SchemaContext(final SchemaTree schema, final NodeType instanceType)
    {
        this.schema = schema;
        this.instanceType = instanceType;
    }

    public SchemaTree getSchema()
    {
        return schema;
    }

    public NodeType getInstanceType()
    {
        return instanceType;
    }

    @Override
    public ProcessingMessage newMessage()
    {
        return new ProcessingMessage().put("schema", schema);
    }
}
