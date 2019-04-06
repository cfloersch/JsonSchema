package xpertss.json.schema.keyword.digest.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Digester for draft v3's {@code properties} keyword
 *
 * <p>This stores the same information as draft v4's {@code required}.</p>
 */
public final class DraftV3PropertiesDigester
    extends AbstractDigester
{
    private static final Digester INSTANCE = new DraftV3PropertiesDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private DraftV3PropertiesDigester()
    {
        super("properties", NodeType.OBJECT);
    }

    @Override
    public JsonNode digest(final JsonNode schema)
    {
        // TODO: return an array directly (same for "required" in v4)
        final ObjectNode ret = FACTORY.objectNode();
        final ArrayNode required = FACTORY.arrayNode();
        ret.put("required", required);

        final JsonNode node = schema.get(keyword);
        final List<String> list = Lists.newArrayList(node.fieldNames());

        Collections.sort(list);

        for (final String field: list)
            if (node.get(field).path("required").asBoolean(false))
                required.add(field);

        return ret;
    }
}
