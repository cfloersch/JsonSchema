package xpertss.json.schema.format.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.math.BigInteger;

/**
 * Validator for the {@code utc-millisec} format attribute.
 *
 * <p>Note that there is no restriction on the number value at all. However,
 * this attributes perform extra checks and <b>warns</b> (ie, does not report
 * an error) in the following situations:</p>
 *
 * <ul>
 *     <li>the number is negative;</li>
 *     <li>the number, divided by 1000, is greater than 2^31 - 1.</li>
 * </ul>
 */
public final class UTCMillisecAttribute
    extends AbstractFormatAttribute
{
    /**
     * The maximum bit length of a Unix timestamp value
     */
    private static final int EPOCH_BITLENGTH = 31;

    /**
     * 1000 as a {@link BigInteger}
     */
    private static final BigInteger ONE_THOUSAND = new BigInteger("1000");

    private static final FormatAttribute INSTANCE = new UTCMillisecAttribute();

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    private UTCMillisecAttribute()
    {
        super("utc-millisec", NodeType.INTEGER, NodeType.NUMBER);
    }

    @Override
    public void validate(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();

        BigInteger epoch = instance.bigIntegerValue();

        if (epoch.signum() == -1) {
            report.warn(newMsg(data, bundle, "warn.format.epoch.negative")
                .putArgument("value", instance));
            return;
        }

        epoch = epoch.divide(ONE_THOUSAND);

        if (epoch.bitLength() > EPOCH_BITLENGTH)
            report.warn(newMsg(data, bundle, "warn.format.epoch.overflow")
                .putArgument("value", instance));
    }
}
