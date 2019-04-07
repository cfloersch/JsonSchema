package xpertss.json.schema.library.validator;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.ReflectionKeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.common.AdditionalItemsValidator;
import xpertss.json.schema.keyword.validator.common.AdditionalPropertiesValidator;
import xpertss.json.schema.keyword.validator.common.EnumValidator;
import xpertss.json.schema.keyword.validator.common.MaxItemsValidator;
import xpertss.json.schema.keyword.validator.common.MaxLengthValidator;
import xpertss.json.schema.keyword.validator.common.MaximumValidator;
import xpertss.json.schema.keyword.validator.common.MinItemsValidator;
import xpertss.json.schema.keyword.validator.common.MinLengthValidator;
import xpertss.json.schema.keyword.validator.common.MinimumValidator;
import xpertss.json.schema.keyword.validator.common.PatternValidator;
import xpertss.json.schema.keyword.validator.common.UniqueItemsValidator;

/**
 * Keyword validator constructors common to draft v4 and v3
 */
public final class CommonValidatorDictionary {

    private static final Dictionary<KeywordValidatorFactory> DICTIONARY;

    private CommonValidatorDictionary()
    {
    }

    public static Dictionary<KeywordValidatorFactory> get()
    {
        return DICTIONARY;
    }

    static {
        DictionaryBuilder<KeywordValidatorFactory> builder = Dictionary.newBuilder();

        String keyword;
        Class<? extends KeywordValidator> c;

        /*
         * Arrays
         */
        keyword = "additionalItems";
        c = AdditionalItemsValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "minItems";
        c = MinItemsValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "maxItems";
        c = MaxItemsValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "uniqueItems";
        c = UniqueItemsValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        /*
         * Numbers and integers
         */
        keyword = "minimum";
        c = MinimumValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "maximum";
        c = MaximumValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        /*
         * Objects
         */
        keyword = "additionalProperties";
        c = AdditionalPropertiesValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        /*
         * Strings
         */
        keyword = "minLength";
        c = MinLengthValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "maxLength";
        c = MaxLengthValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "pattern";
        c = PatternValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "enum";
        c = EnumValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        DICTIONARY = builder.freeze();
    }

    private static KeywordValidatorFactory factory(String name, Class<? extends KeywordValidator> c)
    {
        return new ReflectionKeywordValidatorFactory(name, c);
    }
}
