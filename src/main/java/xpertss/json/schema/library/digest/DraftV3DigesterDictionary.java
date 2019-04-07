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
public final class DraftV3DigesterDictionary {

    private static final Dictionary<Digester> DICTIONARY;

    private DraftV3DigesterDictionary()
    {
    }

    static {
        DictionaryBuilder<Digester> builder = Dictionary.newBuilder();
        builder.addAll(CommonDigesterDictionary.get());

        /*
         * Number / integer
         */
        builder.addEntry("divisibleBy", DivisibleByDigester.getInstance());

        /*
         * Object
         */
        builder.addEntry("properties", DraftV3PropertiesDigester.getInstance());
        builder.addEntry("dependencies", DraftV3DependenciesDigester.getInstance());

        /*
         * All
         */
        builder.addEntry("type", new DraftV3TypeKeywordDigester("type"));
        builder.addEntry("disallow", new DraftV3TypeKeywordDigester("disallow"));
        builder.addEntry("extends", new NullDigester("extends", NodeType.ARRAY, NodeType.values()));

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<Digester> get()
    {
        return DICTIONARY;
    }
}
