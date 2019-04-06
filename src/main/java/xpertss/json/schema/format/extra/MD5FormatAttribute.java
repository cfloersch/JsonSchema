package xpertss.json.schema.format.extra;

import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.helpers.HexStringFormatAttribute;

/**
 * Format specifier for {@code md5}
 *
 * @see HexStringFormatAttribute
 */
public final class MD5FormatAttribute
    extends HexStringFormatAttribute
{
    private static final FormatAttribute instance = new MD5FormatAttribute();

    private MD5FormatAttribute()
    {
        super("md5", 32);
    }

    public static FormatAttribute getInstance()
    {
        return instance;
    }

}
