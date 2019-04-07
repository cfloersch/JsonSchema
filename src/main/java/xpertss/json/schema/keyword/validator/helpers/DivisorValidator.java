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
public abstract class DivisorValidator extends NumericValidator {

    protected DivisorValidator(String keyword, JsonNode digest)
    {
        super(keyword, digest);
    }

    @Override
    protected final void validateLong(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        JsonNode node = data.getInstance().getNode();
        long instanceValue = node.longValue();
        long longValue = number.longValue();

        long remainder = instanceValue % longValue;

        if (remainder == 0L)
            return;

        report.error(newMsg(data, bundle, "err.common.divisor.nonZeroRemainder")
            .putArgument("value", node).putArgument("divisor", number));
    }

    @Override
    protected final void validateDecimal(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        JsonNode node = data.getInstance().getNode();
        BigDecimal instanceValue = node.decimalValue();
        BigDecimal decimalValue = number.decimalValue();

        BigDecimal remainder = instanceValue.remainder(decimalValue);

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
