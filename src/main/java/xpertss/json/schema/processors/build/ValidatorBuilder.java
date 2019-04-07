package xpertss.json.schema.processors.build;

import java.util.Map;
import java.util.SortedMap;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.processors.data.SchemaDigest;
import xpertss.json.schema.processors.data.ValidatorList;
import xpertss.json.schema.processors.validation.ValidationProcessor;
import com.google.common.collect.Maps;

/**
 * Keyword builder processor
 *
 * <p>This takes a {@link SchemaDigest} as an input and outputs a {@link
 * ValidatorList}. The main processor, {@link ValidationProcessor}, then uses
 * this validator list to perform actual instance validation.</p>
 *
 * @see ValidationProcessor
 */
public final class ValidatorBuilder implements Processor<SchemaDigest, ValidatorList> {

    private final Map<String, KeywordValidatorFactory> factories;

    public ValidatorBuilder(Library library)
    {
        factories = library.getValidators().entries();
    }

    public ValidatorBuilder(Dictionary<KeywordValidatorFactory> dict)
    {
        factories = dict.entries();
    }

    /**
     * Process the input
     *
     * @param report the report to use while processing
     * @param input the input for this processor
     * @return the output
     * @throws ProcessingException processing failed
     */
    @Override
    public ValidatorList process(ProcessingReport report, SchemaDigest input)
        throws ProcessingException
    {
        final SortedMap<String, KeywordValidator> map = Maps.newTreeMap();

        String keyword;
        JsonNode digest;
        KeywordValidator validator;
        KeywordValidatorFactory factory;

        for (final Map.Entry<String, JsonNode> entry:
            input.getDigests().entrySet()) {
            keyword = entry.getKey();
            digest = entry.getValue();
            factory = factories.get(keyword);
            validator = factory.getKeywordValidator(digest);
            map.put(keyword, validator);
        }
        return new ValidatorList(input.getContext(), map.values());
    }

    @Override
    public String toString()
    {
        return "validator builder";
    }
}
