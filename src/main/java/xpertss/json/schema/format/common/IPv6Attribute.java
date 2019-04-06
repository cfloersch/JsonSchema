package xpertss.json.schema.format.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.net.InetAddresses;

/**
 * Validator for the {@code ipv6} format attribute.
 *
 * <p>This uses Guava's {@link InetAddresses} to do the job.</p>
 */
public final class IPv6Attribute
    extends AbstractFormatAttribute
{
    private static final int IPV6_LENGTH = 16;

    private static final FormatAttribute INSTANCE = new IPv6Attribute();

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    private IPv6Attribute()
    {
        super("ipv6", NodeType.STRING);
    }

    @Override
    public void validate(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();
        final String ipaddr = instance.textValue();

        if (InetAddresses.isInetAddress(ipaddr) && InetAddresses
            .forString(ipaddr).getAddress().length == IPV6_LENGTH)
            return;

        report.error(newMsg(data, bundle, "err.format.invalidIPV6Address")
            .putArgument("value", ipaddr));
    }
}
