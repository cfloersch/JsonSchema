package xpertss.json.schema.keyword.digest.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.helpers.NumericDigester;

/**
 * Digester for {@code divisibleBy}
 *
 * @see NumericDigester
 */
public final class DivisibleByDigester extends NumericDigester {

    private static final Digester INSTANCE = new DivisibleByDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private DivisibleByDigester()
    {
        super("divisibleBy");
    }

    @Override
    public JsonNode digest(JsonNode schema)
    {
        return digestedNumberNode(schema);
    }
}
