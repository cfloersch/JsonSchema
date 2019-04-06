package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for {@code additionalItems}
 */
public final class AdditionalItemsValidator
    extends AbstractKeywordValidator
{
    private final boolean additionalOK;
    private final int itemsSize;

    public AdditionalItemsValidator(final JsonNode digest)
    {
        super("additionalItems");
        additionalOK = digest.get(keyword).booleanValue();
        itemsSize = digest.get("itemsSize").intValue();
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        if (additionalOK)
            return;

        final int size = data.getInstance().getNode().size();
        if (size > itemsSize)
            report.error(newMsg(data, bundle,
                "err.common.additionalItems.notAllowed")
                .putArgument("allowed", itemsSize).putArgument("found", size));
    }

    @Override
    public String toString()
    {
        return keyword + ": "
            + (additionalOK ? "allowed" : itemsSize + " max");
    }
}
