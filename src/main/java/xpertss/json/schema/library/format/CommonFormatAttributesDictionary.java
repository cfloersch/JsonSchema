package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.common.DateTimeAttribute;
import xpertss.json.schema.format.common.EmailAttribute;
import xpertss.json.schema.format.common.IPv6Attribute;
import xpertss.json.schema.format.common.RegexAttribute;
import xpertss.json.schema.format.common.URIAttribute;

/**
 * Format attributes common to draft v4 and v3
 */
public final class CommonFormatAttributesDictionary {

    private static final Dictionary<FormatAttribute> DICTIONARY;

    private CommonFormatAttributesDictionary()
    {
    }

    static {
        DictionaryBuilder<FormatAttribute> builder = Dictionary.newBuilder();
        builder.addAll(ExtraFormatsDictionary.get());
        builder.addEntry("date-time", DateTimeAttribute.getInstance());
        builder.addEntry("email", EmailAttribute.getInstance());
        builder.addEntry("ipv6", IPv6Attribute.getInstance());
        builder.addEntry("regex", RegexAttribute.getInstance());
        builder.addEntry("uri", URIAttribute.getInstance());

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
