package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.helpers.PositiveIntegerValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for {@code maxItems}
 */
public final class MaxItemsValidator
    extends PositiveIntegerValidator
{
    public MaxItemsValidator(final JsonNode digest)
    {
        super("maxItems", digest);
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
                "err.common.maxItems.arrayTooLarge")
                .putArgument(keyword, intValue).putArgument("found", size));
    }
}
