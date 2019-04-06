package xpertss.json.schema.keyword.digest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.digest.Digester;

/**
 * Digester for {@code additionalItems}
 *
 * <p>The digested form is very simple: additional items are always allowed
 * unless the keword is {@code false} <i>and</i> {@code items} is an array. In
 * this last case, the size of the {@code items} array is stored.</p>
 */
public final class AdditionalItemsDigester
    extends AbstractDigester
{
    private static final Digester INSTANCE
        = new AdditionalItemsDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private AdditionalItemsDigester()
    {
        super("additionalItems", NodeType.ARRAY);
    }

    @Override
    public JsonNode digest(final JsonNode schema)
    {
        final ObjectNode ret = FACTORY.objectNode();

        /*
         * First, let's assume that additionalItems is true or a schema
         */
        ret.put(keyword, true);
        ret.put("itemsSize", 0);

        /*
         * If it is false:
         *
         * - if "items" is an object, nevermind;
         * - but if it is an array, set it to false and include the array size.
         *
         * We use .asBoolean() here since it does what we want: we know the
         * syntax is correct, so this will return false if and only if
         * additionalItems itself is boolean false. We return true as the
         * default value.
         */
        if (schema.get(keyword).asBoolean(true))
            return ret;

        final JsonNode itemsNode = schema.path("items");

        if (!itemsNode.isArray())
            return ret;

        /*
         * OK, "items" is there and it is an array, include its size
         */
        ret.put(keyword, false);
        ret.put("itemsSize", itemsNode.size());
        return ret;
    }
}
