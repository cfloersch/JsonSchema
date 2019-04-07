package xpertss.json.schema.keyword.validator.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.keyword.validator.helpers.DivisorValidator;

/**
 * Keyword validator for draft v3's {@code divisibleBy}
 */
public final class DivisibleByValidator extends DivisorValidator {

    public DivisibleByValidator(JsonNode digest)
    {
        super("divisibleBy", digest);
    }
}
