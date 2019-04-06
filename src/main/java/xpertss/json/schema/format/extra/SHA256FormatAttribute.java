package xpertss.json.schema.format.extra;

import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.helpers.HexStringFormatAttribute;

/**
 * Format specifier for {@code sha256}
 *
 * @see HexStringFormatAttribute
 */
public final class SHA256FormatAttribute
    extends HexStringFormatAttribute
{
    private static final FormatAttribute instance = new SHA256FormatAttribute();

    private SHA256FormatAttribute()
    {
        super("sha256", 64);
    }

    public static FormatAttribute getInstance()
    {
        return instance;
    }

}
