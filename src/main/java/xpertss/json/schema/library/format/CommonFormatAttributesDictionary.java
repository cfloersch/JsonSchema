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
public final class CommonFormatAttributesDictionary
{
    private static final Dictionary<FormatAttribute> DICTIONARY;

    private CommonFormatAttributesDictionary()
    {
    }

    static {
        final DictionaryBuilder<FormatAttribute> builder
            = Dictionary.newBuilder();

        builder.addAll(ExtraFormatsDictionary.get());

        String name;
        FormatAttribute attribute;

        name = "date-time";
        attribute = DateTimeAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "email";
        attribute = EmailAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "ipv6";
        attribute = IPv6Attribute.getInstance();
        builder.addEntry(name, attribute);

        name = "regex";
        attribute = RegexAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "uri";
        attribute = URIAttribute.getInstance();
        builder.addEntry(name, attribute);

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
