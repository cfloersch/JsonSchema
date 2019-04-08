package xpertss.json.schema.format.common;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Validator for the {@code email} format attribute.
 *
 * <p><b>Important note</b>: the basis for email format validation is <a
 * href="http://tools.ietf.org/html/rfc5322">RFC 5322</a>. The RFC mandates that
 * email addresses have a domain part. However, that domain part may consist of
 * a single domain name component. As such, {@code foo@bar} is considered valid.
 * </p>
 */
public final class EmailAttribute extends AbstractFormatAttribute {

    private static final FormatAttribute INSTANCE = new EmailAttribute();

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    private EmailAttribute()
    {
        super("email", NodeType.STRING);
    }

    @Override
    public void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String value = data.getInstance().getNode().textValue();

        try {
            new InternetAddress(value, true);
        } catch (AddressException ignored) {
            report.error(newMsg(data, bundle, "err.format.invalidEmail")
                .putArgument("value", value));
        }
    }
}
