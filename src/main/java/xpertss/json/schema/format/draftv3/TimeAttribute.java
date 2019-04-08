package xpertss.json.schema.format.draftv3;

import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.helpers.AbstractDateFormatAttribute;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

public final class TimeAttribute extends AbstractDateFormatAttribute {
    
    private static final FormatAttribute INSTANCE = new TimeAttribute();

    private TimeAttribute()
    {
        super("time", "HH:mm:ss");
    }

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected DateTimeFormatter getFormatter()
    {
        return new DateTimeFormatterBuilder()
           .appendValue(HOUR_OF_DAY, 2)
           .appendLiteral(':')
           .appendValue(MINUTE_OF_HOUR, 2)
           .appendLiteral(':')
           .appendValue(SECOND_OF_MINUTE, 2)
           .toFormatter(Locale.getDefault(Locale.Category.FORMAT)).withResolverStyle(ResolverStyle.STRICT);
    }
}
