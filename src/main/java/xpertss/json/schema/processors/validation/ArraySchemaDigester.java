package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;

/**
 * JSON Schema digester for an {@link ArraySchemaSelector}
 */
public final class ArraySchemaDigester extends AbstractDigester {

    private static final Digester INSTANCE = new ArraySchemaDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private ArraySchemaDigester()
    {
        super("", NodeType.ARRAY);
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();
        ret.put("itemsSize", 0);
        ret.put("itemsIsArray", false);

        JsonNode itemsNode = schema.path("items");
        JsonNode additionalNode = schema.path("additionalItems");

        boolean hasItems = !itemsNode.isMissingNode();
        boolean hasAdditional = additionalNode.isObject();

        ret.put("hasItems", hasItems);
        ret.put("hasAdditional", hasAdditional);

        if (itemsNode.isArray()) {
            ret.put("itemsIsArray", true);
            ret.put("itemsSize", itemsNode.size());
        }

        return ret;
    }
}
