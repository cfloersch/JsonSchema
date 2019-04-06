package xpertss.json.schema.keyword.validator.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.helpers.PositiveIntegerValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for draft v4's {@code maxProperties}
 */
public final class MaxPropertiesValidator
    extends PositiveIntegerValidator
{
    public MaxPropertiesValidator(final JsonNode digest)
    {
        super("maxProperties", digest);
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final int size = data.getInstance().getNode().size();
        if (size > intValue)
            report.error(newMsg(data, bundle,
                "err.draftv4.maxProperties.tooManyMembers")
                .putArgument("found", size).putArgument("required", intValue));
    }
}
