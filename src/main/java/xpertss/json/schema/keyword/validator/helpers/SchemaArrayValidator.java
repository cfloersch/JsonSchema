package xpertss.json.schema.keyword.validator.helpers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;

/**
 * Helper validator class for keywords whose value is a schema array
 */
public abstract class SchemaArrayValidator
    extends AbstractKeywordValidator
{
    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    protected SchemaArrayValidator(final String keyword)
    {
        super(keyword);
    }

    @Override
    public final String toString()
    {
        return keyword;
    }
}
