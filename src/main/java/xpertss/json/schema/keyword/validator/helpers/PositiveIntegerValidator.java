package xpertss.json.schema.keyword.validator.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;

/**
 * Helper validator class for keywords whose value is a positive integer
 */
public abstract class PositiveIntegerValidator extends AbstractKeywordValidator {

    protected final int intValue;

    protected PositiveIntegerValidator(String keyword, JsonNode digest)
    {
        super(keyword);
        intValue = digest.get(keyword).intValue();
    }

    @Override
    public final String toString()
    {
        return keyword + ": " + intValue;
    }
}
