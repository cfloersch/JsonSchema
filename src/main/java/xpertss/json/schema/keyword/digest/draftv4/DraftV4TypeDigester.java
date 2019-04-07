package xpertss.json.schema.keyword.digest.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;

import java.util.EnumSet;

/**
 * Digester for {@code type} (draft v4)
 *
 * <p>This will store a set of allowed types. It will, for instance, produce the
 * same digested form of these two forms:</p>
 *
 * <ul>
 *     <li>{@code {"type": "string" } }</li>
 *     <li>{@code { "type": [ "string" ] } }</li>
 * </ul>
 */
public final class DraftV4TypeDigester extends AbstractDigester {

    private static final Digester INSTANCE = new DraftV4TypeDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private DraftV4TypeDigester()
    {
        super("type", NodeType.ARRAY, NodeType.values());
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();
        ArrayNode allowedTypes = FACTORY.arrayNode();
        ret.put(keyword, allowedTypes);

        JsonNode node = schema.get(keyword);

        EnumSet<NodeType> typeSet = EnumSet.noneOf(NodeType.class);

        if (node.isTextual()) // Single type
            typeSet.add(NodeType.fromName(node.textValue()));
        else // More than one type
            for (final JsonNode element: node)
                typeSet.add(NodeType.fromName(element.textValue()));

        if (typeSet.contains(NodeType.NUMBER))
            typeSet.add(NodeType.INTEGER);

        /*
         * Note that as this is an enumset, order is guaranteed
         */
        for (final NodeType type: typeSet)
            allowedTypes.add(type.toString());

        return ret;
    }
}
