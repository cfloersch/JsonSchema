package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.RegexECMA262Helper;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for {@code pattern}
 *
 * @see RegexECMA262Helper
 */
public final class PatternValidator extends AbstractKeywordValidator {

    public PatternValidator(final JsonNode digest)
    {
        super("pattern");
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report,
                         MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String regex = data.getSchema().getNode().get(keyword).textValue();
        String value = data.getInstance().getNode().textValue();
        if (!RegexECMA262Helper.regMatch(regex, value))
            report.error(newMsg(data, bundle, "err.common.pattern.noMatch")
                .putArgument("regex", regex).putArgument("string", value));
    }

    @Override
    public String toString()
    {
        return keyword;
    }
}
