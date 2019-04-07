package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * JSON Schema digester for an {@link ObjectSchemaSelector}
 */
public final class ObjectSchemaDigester extends AbstractDigester {

    private static final Digester INSTANCE = new ObjectSchemaDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private ObjectSchemaDigester()
    {
        super("", NodeType.OBJECT);
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();
        ret.put("hasAdditional", schema.path("additionalProperties").isObject());

        Set<String> set;

        ArrayNode node;

        node = FACTORY.arrayNode();
        ret.put("properties", node);

        set = Sets.newHashSet(schema.path("properties").fieldNames());
        for (final String field: Ordering.natural().sortedCopy(set))
            node.add(field);

        node = FACTORY.arrayNode();
        ret.put("patternProperties", node);

        set = Sets.newHashSet(schema.path("patternProperties").fieldNames());
        for (final String field: Ordering.natural().sortedCopy(set))
            node.add(field);

        return ret;
    }
}
