package xpertss.json.schema.keyword.digest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.helpers.NumericDigester;

/**
 * Digester for {@code maximum}
 *
 * <p>This uses {@link NumericDigester} as a base, and also stores information
 * about the presence (or not) of {@code exclusiveMaximum}.</p>
 */
public final class MaximumDigester extends NumericDigester {

    private static final Digester INSTANCE = new MaximumDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private MaximumDigester()
    {
        super("maximum");
    }
    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = digestedNumberNode(schema);
        ret.put("exclusive", schema.path("exclusiveMaximum").asBoolean(false));
        return ret;
    }
}
