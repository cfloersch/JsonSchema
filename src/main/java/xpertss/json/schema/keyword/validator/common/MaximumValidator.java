package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.helpers.NumericValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.math.BigDecimal;

/**
 * Keyword validator for {@code maximum}
 */
public final class MaximumValidator
    extends NumericValidator
{
    private final boolean exclusive;

    public MaximumValidator(final JsonNode digest)
    {
        super("maximum", digest);
        exclusive = digest.path("exclusive").booleanValue();
    }

    @Override
    protected void validateLong(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();
        final long instanceValue = instance.longValue();
        final long longValue = number.longValue();

        if (instanceValue < longValue)
            return;

        if (instanceValue > longValue) {
            report.error(newMsg(data, bundle, "err.common.maximum.tooLarge")
                .putArgument(keyword, number).putArgument("found", instance));
            return;
        }

        if (!exclusive)
            return;

        report.error(newMsg(data, bundle, "err.common.maximum.notExclusive")
            .putArgument(keyword, number)
            .put("exclusiveMaximum", BooleanNode.TRUE));
    }

    @Override
    protected void validateDecimal(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();
        final BigDecimal instanceValue = instance.decimalValue();
        final BigDecimal decimalValue = number.decimalValue();

        final int cmp = instanceValue.compareTo(decimalValue);

        if (cmp < 0)
            return;

        if (cmp > 0) {
            report.error(newMsg(data, bundle, "err.common.maximum.tooLarge")
                .putArgument(keyword, number).putArgument("found", instance));
            return;
        }

        if (!exclusive)
            return;

        report.error(newMsg(data, bundle, "err.common.maximum.notExclusive")
            .putArgument(keyword, number)
            .put("exclusiveMaximum", BooleanNode.TRUE));
    }
}
