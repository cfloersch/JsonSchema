package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.RhinoHelper;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for {@code pattern}
 *
 * @see RhinoHelper
 */
public final class PatternValidator
    extends AbstractKeywordValidator
{
    public PatternValidator(final JsonNode digest)
    {
        super("pattern");
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final String regex = data.getSchema().getNode().get(keyword)
            .textValue();
        final String value = data.getInstance().getNode().textValue();
        if (!RhinoHelper.regMatch(regex, value))
            report.error(newMsg(data, bundle, "err.common.pattern.noMatch")
                .putArgument("regex", regex).putArgument("string", value));
    }

    @Override
    public String toString()
    {
        return keyword;
    }
}
