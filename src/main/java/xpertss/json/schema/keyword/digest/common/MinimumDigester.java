package xpertss.json.schema.keyword.digest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.helpers.NumericDigester;

/**
 * Digester for {@code minimum}
 *
 * <p>This uses {@link NumericDigester} as a base, and also stores information
 * about the presence (or not) of {@code exclusiveMinimum}.</p>
 */
public final class MinimumDigester extends NumericDigester {

    private static final Digester INSTANCE = new MinimumDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private MinimumDigester()
    {
        super("minimum");
    }
    @Override
    public JsonNode digest(JsonNode schema)
    {
        ObjectNode ret = digestedNumberNode(schema);
        ret.put("exclusive", schema.path("exclusiveMinimum").asBoolean(false));
        return ret;
    }
}
