package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.helpers.IPv4FormatAttribute;
import xpertss.json.schema.format.helpers.SharedHostNameAttribute;

/**
 * Draft v4 specific format attributes
 */
public final class DraftV4FormatAttributesDictionary
{
    private static final Dictionary<FormatAttribute> DICTIONARY;

    private DraftV4FormatAttributesDictionary()
    {
    }

    static {
        final DictionaryBuilder<FormatAttribute> builder
            = Dictionary.newBuilder();

        builder.addAll(CommonFormatAttributesDictionary.get());

        builder.addEntry("hostname", new SharedHostNameAttribute("hostname"));

        builder.addEntry("ipv4", new IPv4FormatAttribute("ipv4"));

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
