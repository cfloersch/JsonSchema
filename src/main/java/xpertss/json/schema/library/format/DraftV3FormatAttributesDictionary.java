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
public final class DraftV3FormatAttributesDictionary
{
    private static final Dictionary<FormatAttribute> DICTIONARY;

    private DraftV3FormatAttributesDictionary()
    {
    }

    static {
        final DictionaryBuilder<FormatAttribute> builder
            = Dictionary.newBuilder();

        builder.addAll(CommonFormatAttributesDictionary.get());

        String name;
        FormatAttribute attribute;

        name = "date";
        attribute = DateAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "host-name";
        attribute = new SharedHostNameAttribute("host-name");
        builder.addEntry(name, attribute);

        name = "ip-address";
        attribute = new IPv4FormatAttribute(name);
        builder.addEntry(name, attribute);

        name = "phone";
        attribute = PhoneAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "time";
        attribute = TimeAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "utc-millisec";
        attribute = UTCMillisecAttribute.getInstance();
        builder.addEntry(name, attribute);

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
