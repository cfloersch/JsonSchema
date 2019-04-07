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
 * Keyword validator for {@code minimum}
 */
public final class MinimumValidator extends NumericValidator {

    private final boolean exclusive;

    public MinimumValidator(JsonNode digest)
    {
        super("minimum", digest);
        exclusive = digest.path("exclusive").asBoolean(false);
    }

    @Override
    protected void validateLong(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        JsonNode instance = data.getInstance().getNode();
        long instanceValue = instance.longValue();
        long longValue = number.longValue();

        if (instanceValue > longValue)
            return;

        if (instanceValue < longValue) {
            report.error(newMsg(data, bundle, "err.common.minimum.tooSmall")
                .putArgument(keyword, number).putArgument("found", instance));
            return;
        }

        if (!exclusive)
            return;

        report.error(newMsg(data, bundle, "err.common.minimum.notExclusive")
            .putArgument(keyword, number)
            .put("exclusiveMinimum", BooleanNode.TRUE));
    }

    @Override
    protected void validateDecimal(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        JsonNode instance = data.getInstance().getNode();
        BigDecimal instanceValue = instance.decimalValue();
        BigDecimal decimalValue = number.decimalValue();

        int cmp = instanceValue.compareTo(decimalValue);

        if (cmp > 0)
            return;

        if (cmp < 0) {
            report.error(newMsg(data, bundle, "err.common.minimum.tooSmall")
                .putArgument(keyword, number).putArgument("found", instance));
            return;
        }

        if (!exclusive)
            return;

        report.error(newMsg(data, bundle, "err.common.minimum.notExclusive")
            .putArgument(keyword, number)
            .put("exclusiveMinimum", BooleanNode.TRUE));
    }
}
