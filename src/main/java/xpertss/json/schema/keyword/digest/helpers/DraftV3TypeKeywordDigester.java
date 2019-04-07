package xpertss.json.schema.keyword.digest.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;

import java.util.EnumSet;

/**
 * Digester for draft v3's {@code type} and {@code disallow}
 *
 * <p>These keywords are quite complex, but fortunately they share the same
 * fundamental structure. Simple types and schema dependencies are stored
 * separately.</p>
 */
public final class DraftV3TypeKeywordDigester extends AbstractDigester {

    private static final String ANY = "any";

    public DraftV3TypeKeywordDigester(String keyword)
    {
        super(keyword, NodeType.ARRAY, NodeType.values());
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();
        ArrayNode simpleTypes = FACTORY.arrayNode();
        ret.put(keyword, simpleTypes);
        ArrayNode schemas = FACTORY.arrayNode();
        ret.put("schemas", schemas);

        JsonNode node = schema.get(keyword);

        EnumSet<NodeType> set = EnumSet.noneOf(NodeType.class);

        if (node.isTextual()) // Single type
            putType(set, node.textValue());
        else { // More than one type, and possibly schemas
            final int size = node.size();
            JsonNode element;
            for (int index = 0; index < size; index++) {
                element = node.get(index);
                if (element.isTextual())
                    putType(set, element.textValue());
                else
                    schemas.add(index);
            }
        }

        /*
         * If all types are there, no need to collect schemas
         */
        if (EnumSet.complementOf(set).isEmpty())
            schemas.removeAll();

        /*
         * Note that as this is an enumset, order is guaranteed
         */
        for (final NodeType type: set)
            simpleTypes.add(type.toString());

        return ret;
    }

    private static void putType(EnumSet<NodeType> types, String s)
    {
        if (ANY.equals(s)) {
            types.addAll(EnumSet.allOf(NodeType.class));
            return;
        }

        final NodeType type = NodeType.fromName(s);
        types.add(type);

        if (type == NodeType.NUMBER)
            types.add(NodeType.INTEGER);
    }
}
