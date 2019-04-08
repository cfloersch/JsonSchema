package xpertss.json.schema.library.format;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.format.extra.Base64FormatAttribute;
import xpertss.json.schema.format.extra.CurrencyFormatAttribute;
import xpertss.json.schema.format.draftv6.JsonPointerFormatAttribute;
import xpertss.json.schema.format.extra.MD5FormatAttribute;
import xpertss.json.schema.format.extra.MacAddressFormatAttribute;
import xpertss.json.schema.format.extra.SHA1FormatAttribute;
import xpertss.json.schema.format.extra.SHA256FormatAttribute;
import xpertss.json.schema.format.extra.SHA512FormatAttribute;
import xpertss.json.schema.format.extra.UUIDFormatAttribute;

public final class ExtraFormatsDictionary {

    private static final Dictionary<FormatAttribute> DICTIONARY;

    private ExtraFormatsDictionary()
    {
    }

    static {
        DictionaryBuilder<FormatAttribute> builder = Dictionary.newBuilder();

        builder.addEntry("base64", Base64FormatAttribute.getInstance());
        builder.addEntry("mac", MacAddressFormatAttribute.getInstance());
        builder.addEntry("md5", MD5FormatAttribute.getInstance());
        builder.addEntry("sha1", SHA1FormatAttribute.getInstance());
        builder.addEntry("sha256", SHA256FormatAttribute.getInstance());
        builder.addEntry("sha512", SHA512FormatAttribute.getInstance());
        builder.addEntry("uuid", UUIDFormatAttribute.getInstance());
        builder.addEntry("currency", CurrencyFormatAttribute.getInstance());

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<FormatAttribute> get()
    {
        return DICTIONARY;
    }
}
