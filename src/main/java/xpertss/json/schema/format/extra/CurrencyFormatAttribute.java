package xpertss.json.schema.format.extra;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Currency;

/**
 * Format specifier for a proposed {@code currency} attribute
 *
 * @see Currency#getInstance(String)
 */
public final class CurrencyFormatAttribute extends AbstractFormatAttribute {

    private static final FormatAttribute instance = new CurrencyFormatAttribute();

    private CurrencyFormatAttribute()
    {
        super("currency", NodeType.STRING);
    }

    public static FormatAttribute getInstance()
    {
        return instance;
    }

    @Override
    public void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String input = data.getInstance().getNode().textValue();

        try {
            Currency.getInstance(input);
        } catch (IllegalArgumentException ignored) {
            report.error(newMsg(data, bundle, "err.format.currency.invalid")
                .putArgument("value", input));
        }
    }
}
