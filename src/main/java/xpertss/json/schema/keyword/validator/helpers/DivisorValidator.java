package xpertss.json.schema.keyword.validator.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.math.BigDecimal;

/**
 * Helper keyword validator for draft v4's {@code multipleOf} and draft v3's
 * {@code divisibleBy}
 */
public abstract class DivisorValidator
    extends NumericValidator
{
    protected DivisorValidator(final String keyword, final JsonNode digest)
    {
        super(keyword, digest);
    }

    @Override
    protected final void validateLong(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final JsonNode node = data.getInstance().getNode();
        final long instanceValue = node.longValue();
        final long longValue = number.longValue();

        final long remainder = instanceValue % longValue;

        if (remainder == 0L)
            return;

        report.error(newMsg(data, bundle, "err.common.divisor.nonZeroRemainder")
            .putArgument("value", node).putArgument("divisor", number));
    }

    @Override
    protected final void validateDecimal(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final JsonNode node = data.getInstance().getNode();
        final BigDecimal instanceValue = node.decimalValue();
        final BigDecimal decimalValue = number.decimalValue();

        final BigDecimal remainder = instanceValue.remainder(decimalValue);

        /*
         * We cannot use equality! As far as BigDecimal goes,
         * "0" and "0.0" are NOT equal. But .compareTo() returns the correct
         * result.
         */
        if (remainder.compareTo(BigDecimal.ZERO) == 0)
            return;

        report.error(newMsg(data, bundle, "err.common.divisor.nonZeroRemainder")
            .putArgument("value", node).putArgument("divisor", number));
    }
}
