package xpertss.json.schema.format.helpers;

import com.github.fge.jackson.NodeType;
import com.google.common.collect.Sets;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Abstract class for date/time related format attributes
 */
public abstract class AbstractDateFormatAttribute
    extends AbstractFormatAttribute
{
    private final String[] formats;

    protected AbstractDateFormatAttribute(final String fmt, final String ... formats)
    {
        super(fmt, NodeType.STRING);
        this.formats = formats;
    }

    protected abstract DateTimeFormatter getFormatter();

    @Override
    public final void validate(final ProcessingReport report,
        final MessageBundle bundle, final FullData data)
        throws ProcessingException
    {
        final DateTimeFormatter formatter = getFormatter();
        final String value = data.getInstance().getNode().textValue();

        try {
            formatter.parse(value);
        } catch (DateTimeParseException ignored) {
            report.error(newMsg(data, bundle, "err.format.invalidDate")
               .putArgument("value", value).putArgument("expected", Sets.newHashSet(formats)));
        }
    }
}
