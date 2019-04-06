package xpertss.json.schema.format.helpers;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.net.InetAddresses;

/**
 * Validator for both the {@code ip-address} (draft v3) and {@code ipv4} (draft
 * v4) format attributes.
 *
 * <p>This uses Guava's {@link InetAddresses} to do the job.</p>
 */
public final class IPv4FormatAttribute
    extends AbstractFormatAttribute
{
    private static final int IPV4_LENGTH = 4;

    public IPv4FormatAttribute(final String fmt)
    {
        super(fmt, NodeType.STRING);
    }

    @Override
    public void validate(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final String ipaddr = data.getInstance().getNode().textValue();

        if (InetAddresses.isInetAddress(ipaddr) && InetAddresses
            .forString(ipaddr).getAddress().length == IPV4_LENGTH)
            return;

        report.error(newMsg(data, bundle, "err.format.invalidIPv4Address")
            .putArgument("value", ipaddr));
    }
}
