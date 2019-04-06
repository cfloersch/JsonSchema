package xpertss.json.schema.format.draftv7;

import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.helpers.AbstractDateFormatAttribute;

import java.time.format.DateTimeFormatter;

public final class DateAttribute
    extends AbstractDateFormatAttribute
{
    private static final FormatAttribute INSTANCE = new DateAttribute();

    private DateAttribute()
    {
        super("date", "yyyy-MM-dd");
    }

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected DateTimeFormatter getFormatter()
    {
        return DateTimeFormatter.ISO_LOCAL_DATE;
    }
}
