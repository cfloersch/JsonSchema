package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.extra.Base64FormatAttribute;
import xpertss.json.schema.format.extra.CurrencyFormatAttribute;
import xpertss.json.schema.format.extra.JsonPointerFormatAttribute;
import xpertss.json.schema.format.extra.MD5FormatAttribute;
import xpertss.json.schema.format.extra.MacAddressFormatAttribute;
import xpertss.json.schema.format.extra.SHA1FormatAttribute;
import xpertss.json.schema.format.extra.SHA256FormatAttribute;
import xpertss.json.schema.format.extra.SHA512FormatAttribute;
import xpertss.json.schema.format.extra.UUIDFormatAttribute;

public final class ExtraFormatsDictionary
{
    private static final Dictionary<FormatAttribute> DICTIONARY;

    private ExtraFormatsDictionary()
    {
    }

    static {
        final DictionaryBuilder<FormatAttribute> builder
            = Dictionary.newBuilder();

        String name;
        FormatAttribute attribute;

        name = "base64";
        attribute = Base64FormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "json-pointer";
        attribute = JsonPointerFormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "mac";
        attribute = MacAddressFormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "md5";
        attribute = MD5FormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "sha1";
        attribute = SHA1FormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "sha256";
        attribute = SHA256FormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "sha512";
        attribute = SHA512FormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "uuid";
        attribute = UUIDFormatAttribute.getInstance();
        builder.addEntry(name, attribute);

        name = "currency";
        attribute = CurrencyFormatAttribute.getInstance();
        builder.addEntry(name, attribute);


        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
