package xpertss.json.schema.format.extra;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.UUID;

/**
 * Format specifier for a proposed {@code uuid} attribute
 *
 * @see UUID#fromString(String)
 */
public final class UUIDFormatAttribute
    extends AbstractFormatAttribute
{
    private static final FormatAttribute instance = new UUIDFormatAttribute();

    private UUIDFormatAttribute()
    {
        super("uuid", NodeType.STRING);
    }

    public static FormatAttribute getInstance()
    {
        return instance;
    }

    @Override
    public void validate(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final String input = data.getInstance().getNode().textValue();

        try {
            UUID.fromString(input);
        } catch (IllegalArgumentException ignored) {
            report.error(newMsg(data, bundle, "err.format.UUID.invalid")
                .putArgument("value", input));
        }
    }
}
