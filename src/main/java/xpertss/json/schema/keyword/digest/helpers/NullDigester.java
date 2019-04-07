package xpertss.json.schema.keyword.digest.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;

/**
 * A digester returning a {@link NullNode} for any input
 *
 * <p>This is actually useful for keywords like {@code anyOf}, {@code allOf} and
 * {@code oneOf}, which only roles are to validate subschemas: they do not need
 * a digested form at all, they just have to peek at the schema.</p>
 *
 * <p>A net result of all keywords using this digester is that only one instance
 * will ever be built.</p>
 */
public final class NullDigester extends AbstractDigester {

    public NullDigester(String keyword, NodeType first, NodeType... other)
    {
        super(keyword, first, other);
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        return FACTORY.nullNode();
    }
}
