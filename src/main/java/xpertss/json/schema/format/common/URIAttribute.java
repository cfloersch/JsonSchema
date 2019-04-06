package xpertss.json.schema.format.common;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Validator for the {@code uri} format attribute.
 *
 * <p>Note that each and any URI is allowed. In particular, it is not required
 * that the URI be absolute or normalized.</p>
 */
public final class URIAttribute
    extends AbstractFormatAttribute
{
    private static final FormatAttribute INSTANCE = new URIAttribute();

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    private URIAttribute()
    {
        super("uri", NodeType.STRING);
    }

    @Override
    public void validate(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final String value = data.getInstance().getNode().textValue();

        try {
            new URI(value);
        } catch (URISyntaxException ignored) {
            report.error(newMsg(data, bundle, "err.format.invalidURI")
                .putArgument("value", value));
        }
    }
}
