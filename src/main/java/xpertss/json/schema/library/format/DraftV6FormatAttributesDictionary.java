package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.draftv6.JsonPointerFormatAttribute;
import xpertss.json.schema.format.draftv6.URITemplateFormatAttribute;

/**
 * Draft v4 specific format attributes
 */
public final class DraftV6FormatAttributesDictionary {

    private static final Dictionary<FormatAttribute> DICTIONARY;

    private DraftV6FormatAttributesDictionary()
    {
    }

    static {
        DictionaryBuilder<FormatAttribute> builder = Dictionary.newBuilder();

        builder.addAll(DraftV4FormatAttributesDictionary.get());
        builder.addEntry("json-pointer", JsonPointerFormatAttribute.getInstance());
        builder.addEntry("uri-template", URITemplateFormatAttribute.getInstance());


        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
