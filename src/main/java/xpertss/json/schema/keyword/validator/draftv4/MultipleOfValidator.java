package xpertss.json.schema.keyword.validator.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.keyword.validator.helpers.DivisorValidator;

/**
 * Keyword validator for draft v4's {@code multipleOf}
 */
public final class MultipleOfValidator
    extends DivisorValidator
{
    public MultipleOfValidator(final JsonNode digest)
    {
        super("multipleOf", digest);
    }
}
