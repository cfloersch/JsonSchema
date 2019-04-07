package xpertss.json.schema.library.digest;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.digest.common.AdditionalItemsDigester;
import xpertss.json.schema.keyword.digest.common.AdditionalPropertiesDigester;
import xpertss.json.schema.keyword.digest.common.MaximumDigester;
import xpertss.json.schema.keyword.digest.common.MinimumDigester;
import xpertss.json.schema.keyword.digest.helpers.NullDigester;
import xpertss.json.schema.keyword.digest.helpers.SimpleDigester;

import static com.github.fge.jackson.NodeType.*;

/**
 * Digesters common to draft v4 and v3
 */
public final class CommonDigesterDictionary {

    private static final Dictionary<Digester> DICTIONARY;

    private CommonDigesterDictionary()
    {
    }

    static {
        DictionaryBuilder<Digester> builder = Dictionary.newBuilder();

        /*
         * Array
         */
        builder.addEntry("additionalItems", AdditionalItemsDigester.getInstance());
        builder.addEntry("minItems", new SimpleDigester("minItems", ARRAY));
        builder.addEntry("maxItems", new SimpleDigester("maxItems", ARRAY));
        builder.addEntry("uniqueItems", new SimpleDigester("uniqueItems", ARRAY));

        /*
         * Number / Integer
         */
        builder.addEntry("minimum", MinimumDigester.getInstance());
        builder.addEntry("maximum", MaximumDigester.getInstance());

        /*
         * Object
         */
        builder.addEntry("additionalProperties", AdditionalPropertiesDigester.getInstance());

        /*
         * String
         */
        builder.addEntry("minLength", new SimpleDigester("minLength", STRING));
        builder.addEntry("maxLength", new SimpleDigester("maxLength", STRING));
        builder.addEntry("pattern", new NullDigester("pattern", STRING));

        /*
         * Any
         */

        /*
         * FIXME: not perfect
         *
         * Right now we take the node as is, and all the real work is done by
         * the validator. That is:
         *
         * - { "enum": [ 1 ] } and { "enum": [ 1.0 ] } are not the same;
         * - { "enum": [ 1, 2 ] } and { "enum": [ 2, 1 ] } are not the same
         *   either.
         *
         * All these differences are sorted out by the runtime checking, not
         * here. This is kind of a waste, but making just these two above
         * examples yield the same digest would require not only normalizing
         * (for the first case), but also ordering (for the second case).
         *
         * And we don't even get into the territory of other node types here.
         *
         * Bah. There will be duplicates, but at least ultimately the validator
         * will do what it takes.
         */
        builder.addEntry("enum", new SimpleDigester("enum", ARRAY, values()));

        DICTIONARY = builder.freeze();
    }

    public static Dictionary<Digester> get()
    {
        return DICTIONARY;
    }
}
