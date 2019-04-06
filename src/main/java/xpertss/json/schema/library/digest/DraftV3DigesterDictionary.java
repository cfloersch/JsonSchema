package xpertss.json.schema.library.digest;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.draftv3.DivisibleByDigester;
import xpertss.json.schema.keyword.digest.draftv3.DraftV3DependenciesDigester;
import xpertss.json.schema.keyword.digest.draftv3.DraftV3PropertiesDigester;
import xpertss.json.schema.keyword.digest.helpers.DraftV3TypeKeywordDigester;
import xpertss.json.schema.keyword.digest.helpers.NullDigester;

/**
 * Draft v3 specific digesters
 */
public final class DraftV3DigesterDictionary
{
    private static final Dictionary<Digester> DICTIONARY;

    private DraftV3DigesterDictionary()
    {
    }

    static {
        final DictionaryBuilder<Digester> builder
            = Dictionary.newBuilder();

        String keyword;
        Digester digester;

        builder.addAll(CommonDigesterDictionary.get());

        /*
         * Number / integer
         */
        keyword = "divisibleBy";
        digester = DivisibleByDigester.getInstance();
        builder.addEntry(keyword, digester);

        /*
         * Object
         */
        keyword = "properties";
        digester = DraftV3PropertiesDigester.getInstance();
        builder.addEntry(keyword, digester);

        keyword = "dependencies";
        digester = DraftV3DependenciesDigester.getInstance();
        builder.addEntry(keyword, digester);

        /*
         * All
         */
        keyword = "type";
        digester = new DraftV3TypeKeywordDigester(keyword);
        builder.addEntry(keyword, digester);

        keyword = "disallow";
        digester = new DraftV3TypeKeywordDigester(keyword);
        builder.addEntry(keyword, digester);

        keyword = "extends";
        digester = new NullDigester(keyword, NodeType.ARRAY, NodeType.values());
        builder.addEntry(keyword, digester);

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<Digester> get()
    {
        return DICTIONARY;
    }
}
