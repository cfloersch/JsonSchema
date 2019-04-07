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
public final class DraftV4DigesterDictionary {

    private static final Dictionary<Digester> DICTIONARY;

    private DraftV4DigesterDictionary()
    {
    }

    static {
        DictionaryBuilder<Digester> builder = Dictionary.newBuilder();

        String keyword;
        Digester digester;

        builder.addAll(CommonDigesterDictionary.get());

        /*
         * Number/integer
         */
        builder.addEntry("multipleOf", MultipleOfDigester.getInstance());

        /*
         * Object
         */
        builder.addEntry("minProperties", new SimpleDigester("minProperties", OBJECT));
        builder.addEntry("maxProperties", new SimpleDigester("maxProperties", OBJECT));
        builder.addEntry("required", RequiredDigester.getInstance());
        builder.addEntry("dependencies", DraftV4DependenciesDigester.getInstance());

        /*
         * All/none
         */
        builder.addEntry("anyOf", new NullDigester("anyOf", ARRAY, values()));
        builder.addEntry("allOf", new NullDigester("allOf", ARRAY, values()));
        builder.addEntry("oneOf", new NullDigester("oneOf", ARRAY, values()));
        builder.addEntry("not", new NullDigester("not", ARRAY, values()));
        builder.addEntry("type", DraftV4TypeDigester.getInstance());

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<Digester> get()
    {
        return DICTIONARY;
    }
}
