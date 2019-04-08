package xpertss.json.schema.format.helpers;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.net.InternetDomainName;

/**
 * Validator for the {@code host-name} format attribute.
 *
 * <p><b>Important note</b>: the basis for host name format validation is <a
 * href="http://tools.ietf.org/html/rfc1034">RFC 1034</a>. The RFC does <b>not
 * </b> require that a host name have more than one domain name component. As
 * such, {@code foo} <b>is</b> a valid hostname.</p>
 *
 * <p>Guava's {@link InternetDomainName} is used for validation.</p>
 */
public final class SharedHostNameAttribute extends AbstractFormatAttribute {

    public SharedHostNameAttribute(String fmt)
    {
        super(fmt, NodeType.STRING);
    }

    @Override
    public void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String value = data.getInstance().getNode().textValue();

        try {
            InternetDomainName.from(value);
        } catch (IllegalArgumentException ignored) {
            report.error(newMsg(data, bundle, "err.format.invalidHostname")
                .putArgument("value", value));
        }
    }
}
