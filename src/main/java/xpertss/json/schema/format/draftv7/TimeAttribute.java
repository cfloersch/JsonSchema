package xpertss.json.schema.format.draftv7;

import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.helpers.AbstractDateFormatAttribute;


import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

public final class TimeAttribute extends AbstractDateFormatAttribute {

    private static final FormatAttribute INSTANCE = new TimeAttribute();

    private TimeAttribute()
    {
        super("time", "HH:mm:ss((+|-)HH:mm|Z)", "HH:mm:ss.[0-9]{1,12}((+|-)HH:mm|Z)");
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
           .optionalStart()
           .appendFraction(NANO_OF_SECOND, 0, 12, true)
           .optionalEnd()
           .appendOffset("+HH:MM", "Z")
           .toFormatter(Locale.getDefault(Locale.Category.FORMAT))
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
}
