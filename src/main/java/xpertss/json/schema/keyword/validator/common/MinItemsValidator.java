package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.helpers.PositiveIntegerValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for {@code minItems}
 */
public final class MinItemsValidator extends PositiveIntegerValidator {

    public MinItemsValidator(JsonNode digest)
    {
        super("minItems", digest);
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report,
                         MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        int size = data.getInstance().getNode().size();
        if (size < intValue)
            report.error(newMsg(data, bundle,
                "err.common.minItems.arrayTooShort")
                .putArgument(keyword, intValue).putArgument("found", size));
    }
}
