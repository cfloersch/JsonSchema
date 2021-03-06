package xpertss.json.schema.keyword.digest.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Digester for {@code dependencies}
 *
 * <p>This stores property dependencies separately from schema dependencies.</p>
 */
public final class DraftV3DependenciesDigester extends AbstractDigester {

    private static final Digester INSTANCE = new DraftV3DependenciesDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private DraftV3DependenciesDigester()
    {
        super("dependencies", NodeType.OBJECT);
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();

        ObjectNode propertyDeps = FACTORY.objectNode();
        ret.put("propertyDeps", propertyDeps);

        ArrayNode schemaDeps = FACTORY.arrayNode();
        ret.put("schemaDeps", schemaDeps);

        List<String> list = Lists.newArrayList();

        Map<String, JsonNode> map = JacksonUtils.asMap(schema.get(keyword));

        String key;
        JsonNode value;
        NodeType type;

        for (final Map.Entry<String, JsonNode> entry: map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            type = NodeType.getNodeType(value);
            switch (type) {
                case OBJECT:
                    list.add(key);
                    break;
                case ARRAY:
                    final JsonNode node = sortedSet(value);
                    if (node.size() != 0)
                        propertyDeps.put(key, node);
                    break;
                case STRING:
                    propertyDeps.put(key, FACTORY.arrayNode()
                        .add(value.textValue()));
            }
        }

        for (final String s: Ordering.natural().sortedCopy(list))
            schemaDeps.add(s);

        return ret;
    }

    private static JsonNode sortedSet(JsonNode node)
    {
        SortedSet<JsonNode> set = Sets.newTreeSet(Comparator.comparing(JsonNode::textValue));

        set.addAll(Sets.newHashSet(node));
        ArrayNode ret = FACTORY.arrayNode();
        ret.addAll(set);
        return ret;
    }
}
