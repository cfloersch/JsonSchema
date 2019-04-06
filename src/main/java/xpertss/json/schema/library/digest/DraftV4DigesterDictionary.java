package xpertss.json.schema.library.digest;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.draftv4.DraftV4DependenciesDigester;
import xpertss.json.schema.keyword.digest.draftv4.DraftV4TypeDigester;
import xpertss.json.schema.keyword.digest.draftv4.MultipleOfDigester;
import xpertss.json.schema.keyword.digest.draftv4.RequiredDigester;
import xpertss.json.schema.keyword.digest.helpers.NullDigester;
import xpertss.json.schema.keyword.digest.helpers.SimpleDigester;

import static com.github.fge.jackson.NodeType.*;

/**
 * Draft v4 specific digesters
 */
public final class DraftV4DigesterDictionary
{
    private static final Dictionary<Digester> DICTIONARY;

    private DraftV4DigesterDictionary()
    {
    }

    static {
        final DictionaryBuilder<Digester> builder
            = Dictionary.newBuilder();

        String keyword;
        Digester digester;

        builder.addAll(CommonDigesterDictionary.get());

        /*
         * Number/integer
         */
        keyword = "multipleOf";
        digester = MultipleOfDigester.getInstance();
        builder.addEntry(keyword, digester);

        /*
         * Object
         */
        keyword = "minProperties";
        digester = new SimpleDigester(keyword, OBJECT);
        builder.addEntry(keyword, digester);

        keyword = "maxProperties";
        digester = new SimpleDigester(keyword, OBJECT);
        builder.addEntry(keyword, digester);

        keyword = "required";
        digester = RequiredDigester.getInstance();
        builder.addEntry(keyword, digester);

        keyword = "dependencies";
        digester = DraftV4DependenciesDigester.getInstance();
        builder.addEntry(keyword, digester);

        /*
         * All/none
         */
        keyword = "anyOf";
        digester = new NullDigester(keyword, ARRAY, values());
        builder.addEntry(keyword, digester);

        keyword = "allOf";
        digester = new NullDigester(keyword, ARRAY, values());
        builder.addEntry(keyword, digester);

        keyword = "oneOf";
        digester = new NullDigester(keyword, ARRAY, values());
        builder.addEntry(keyword, digester);

        keyword = "not";
        digester = new NullDigester(keyword, ARRAY, values());
        builder.addEntry(keyword, digester);

        keyword = "type";
        digester = DraftV4TypeDigester.getInstance();
        builder.addEntry(keyword, digester);

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<Digester> get()
    {
        return DICTIONARY;
    }
}
