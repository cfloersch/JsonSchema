package xpertss.json.schema.keyword.validator;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;

/**
 * Interface for a keyword validator factory
 */
public interface KeywordValidatorFactory
{
    /**
     * Create a validator for the instance
     *
     * @param node the instance to validate
     * @return a validator for the given instance
     * @throws ProcessingException an error occurs creating the validator
     */
    KeywordValidator getKeywordValidator(JsonNode node)
            throws ProcessingException;
}
