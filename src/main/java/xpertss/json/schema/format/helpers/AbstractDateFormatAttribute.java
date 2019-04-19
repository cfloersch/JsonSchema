package xpertss.json.schema.format.helpers;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Abstract class for date/time related format attributes
 */
public abstract class AbstractDateFormatAttribute extends AbstractFormatAttribute {

    private final String[] formats;

    protected AbstractDateFormatAttribute(String fmt, String ... formats)
    {
        super(fmt, NodeType.STRING);
        this.formats = formats;
    }

    protected abstract DateTimeFormatter getFormatter();

    @Override
    public final void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        DateTimeFormatter formatter = getFormatter();
        String value = data.getInstance().getNode().textValue();

        try {
            formatter.parse(value);
        } catch (DateTimeParseException ignored) {
            // TODO Catch exception and determine if the issue is format or actual non-existent dates
            report.error(newMsg(data, bundle, "err.format.invalidDate")
               .putArgument("value", value).putArgument("expected", Arrays.asList(formats)));
        }
    }
}
