package xpertss.json.schema.keyword.digest.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;

/**
 * A digester only returning the node associated with the keyword
 *
 * <p>As with all digesters, however, you are required to specify what types
 * this keyword supports.</p>
 */
public final class SimpleDigester extends AbstractDigester {

    public SimpleDigester(String keyword, NodeType first, NodeType... other)
    {
        super(keyword, first, other);
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();
        ret.put(keyword, schema.get(keyword));
        return ret;
    }
}
