package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.helpers.PositiveIntegerValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for {@code maxLength}
 */
public final class MaxLengthValidator extends PositiveIntegerValidator {

    public MaxLengthValidator(JsonNode digested)
    {
        super("maxLength", digested);
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report,
                         MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String value = data.getInstance().getNode().textValue();
        int size = value.codePointCount(0, value.length());

        if (size > intValue)
            report.error(newMsg(data, bundle, "err.common.maxLength.tooLong")
                .putArgument("value", value).putArgument("found", size)
                .putArgument(keyword, intValue));
    }
}
