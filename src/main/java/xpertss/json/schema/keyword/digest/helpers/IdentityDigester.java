package xpertss.json.schema.keyword.digest.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;

/**
 * A "no-op" digester
 *
 * <p>This is the most simple of digesters, which will return its input as the
 * digested forms. If you choose to use it, you will therefore only have to
 * provide the list of types supported by your keyword.</p>
 */
public final class IdentityDigester
    extends AbstractDigester
{
    /**
     * Constructor
     *
     * @param keyword the name for this keyword
     * @param first the first supported type
     * @param other other supported types, if any
     */
    public IdentityDigester(final String keyword, final NodeType first,
        final NodeType... other)
    {
        super(keyword, first, other);
    }

    @Override
    public JsonNode digest(final JsonNode schema)
    {
        return schema;
    }
}
