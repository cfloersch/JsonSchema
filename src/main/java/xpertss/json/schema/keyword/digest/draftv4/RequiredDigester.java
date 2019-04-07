package xpertss.json.schema.keyword.digest.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Digester for {@code required}
 *
 * <p>Its role is simply enough to ensure that, for instance, {@code
 * ["a", "b"]} and {@code ["b", "a"]} produce the same output.</p>
 */
public final class RequiredDigester extends AbstractDigester {

    private static final Digester INSTANCE = new RequiredDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private RequiredDigester()
    {
        super("required", NodeType.OBJECT);
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();
        ArrayNode required = FACTORY.arrayNode();
        ret.put(keyword, required);

        List<JsonNode> list = Lists.newArrayList(schema.get(keyword));

        Collections.sort(list, Comparator.comparing(JsonNode::textValue));

        required.addAll(list);
        return ret;
    }
}
