package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.draftv7.DateAttribute;
import xpertss.json.schema.format.draftv7.TimeAttribute;

/**
 * Draft v4 specific format attributes
 */
public final class DraftV7FormatAttributesDictionary
{
    private static final Dictionary<FormatAttribute> DICTIONARY;

    private DraftV7FormatAttributesDictionary()
    {
    }

    static {
        final DictionaryBuilder<FormatAttribute> builder
            = Dictionary.newBuilder();

        builder.addAll(DraftV6FormatAttributesDictionary.get());

        builder.addEntry("time", TimeAttribute.getInstance());
        builder.addEntry("date", DateAttribute.getInstance());

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
