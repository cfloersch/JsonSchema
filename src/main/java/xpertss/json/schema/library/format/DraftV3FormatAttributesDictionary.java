package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.draftv3.DateAttribute;
import xpertss.json.schema.format.draftv3.PhoneAttribute;
import xpertss.json.schema.format.draftv3.TimeAttribute;
import xpertss.json.schema.format.draftv3.UTCMillisecAttribute;
import xpertss.json.schema.format.helpers.IPv4FormatAttribute;
import xpertss.json.schema.format.helpers.SharedHostNameAttribute;

/**
 * Draft v3 specific format attributes
 */
public final class DraftV3FormatAttributesDictionary {

    private static final Dictionary<FormatAttribute> DICTIONARY;

    private DraftV3FormatAttributesDictionary()
    {
    }

    static {
        DictionaryBuilder<FormatAttribute> builder = Dictionary.newBuilder();

        builder.addAll(CommonFormatAttributesDictionary.get());
        builder.addEntry("date", DateAttribute.getInstance());
        builder.addEntry("time", TimeAttribute.getInstance());
        builder.addEntry("host-name", new SharedHostNameAttribute("host-name"));
        builder.addEntry("ip-address", new IPv4FormatAttribute("ip-address"));
        builder.addEntry("phone", PhoneAttribute.getInstance());
        builder.addEntry("utc-millisec", UTCMillisecAttribute.getInstance());

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
