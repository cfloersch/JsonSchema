package xpertss.json.schema.format.common;

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

/**
 * Validator for the {@code date-time} format attribute
 */
public final class DateTimeAttribute extends AbstractDateFormatAttribute {

    private static final FormatAttribute INSTANCE = new DateTimeAttribute();


    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    // "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,12}Z"

    private DateTimeAttribute()
    {
        super("date-time", "yyyy-MM-dd'T'HH:mm:ss((+|-)HH:mm|Z)", "yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,9}((+|-)HH:mm|Z)");
    }

    @Override
    protected DateTimeFormatter getFormatter()
    {
        return new DateTimeFormatterBuilder()
           .parseCaseInsensitive()
           .append(DateTimeFormatter.ISO_LOCAL_DATE)
           .appendLiteral('T')
           .appendValue(HOUR_OF_DAY, 2)
           .appendLiteral(':')
           .appendValue(MINUTE_OF_HOUR, 2)
           .appendLiteral(':')
           .appendValue(SECOND_OF_MINUTE, 2)
           .optionalStart()
           .appendFraction(NANO_OF_SECOND, 0, 9, true)
           .optionalEnd()
           .appendOffset("+HH:mm", "Z")
           .toFormatter(Locale.getDefault(Locale.Category.FORMAT))
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);

        //return DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    }

}
