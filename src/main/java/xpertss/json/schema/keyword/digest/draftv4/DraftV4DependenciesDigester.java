package xpertss.json.schema.keyword.digest.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Digester for {@code dependencies}
 *
 * <p>This stores property dependencies separately from schema dependencies.</p>
 */
public final class DraftV4DependenciesDigester extends AbstractDigester {

    private static final Digester INSTANCE = new DraftV4DependenciesDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private DraftV4DependenciesDigester()
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

        for (final Map.Entry<String, JsonNode> entry: map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value.isObject()) // schema dep
                list.add(key);
            else // property dep
                propertyDeps.put(key, sortedSet(value));
        }

        for (final String s: Ordering.natural().sortedCopy(list))
            schemaDeps.add(s);

        return ret;
    }

    private static JsonNode sortedSet(JsonNode node)
    {
        List<JsonNode> list = Lists.newArrayList(node);

        Collections.sort(list, Comparator.comparing(JsonNode::textValue));

        ArrayNode ret = FACTORY.arrayNode();
        ret.addAll(list);
        return ret;
    }
}
