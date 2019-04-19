package xpertss.json.schema.format.common;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.RegexECMA262Helper;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Validator for the {@code regex} format attribute.
 *
 * <p>Again, here, we do <b>not</b> use {@link java.util.regex} because it does
 * not fit the bill.</p>
 *
 * @see RegexECMA262Helper
 */
public final class RegexAttribute extends AbstractFormatAttribute {
    
    private static final FormatAttribute INSTANCE = new RegexAttribute();

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    private RegexAttribute()
    {
        super("regex", NodeType.STRING);
    }

    @Override
    public void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String value = data.getInstance().getNode().textValue();

        if (!RegexECMA262Helper.regexIsValid(value))
            report.error(newMsg(data, bundle, "err.format.invalidRegex")
                .putArgument("value", value));
    }
}
