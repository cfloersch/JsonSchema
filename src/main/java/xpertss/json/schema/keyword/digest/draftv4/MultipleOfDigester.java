package xpertss.json.schema.keyword.digest.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.helpers.NumericDigester;

/**
 * Digester for {@code multipleOf}
 *
 * @see NumericDigester
 */
public final class MultipleOfDigester
    extends NumericDigester
{
    private static final Digester INSTANCE = new MultipleOfDigester();

    public static Digester getInstance()
    {
        return INSTANCE;
    }

    private MultipleOfDigester()
    {
        super("multipleOf");
    }

    @Override
    public JsonNode digest(final JsonNode schema)
    {
        return digestedNumberNode(schema);
    }
}
