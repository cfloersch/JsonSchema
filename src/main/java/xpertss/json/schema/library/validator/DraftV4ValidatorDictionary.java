package xpertss.json.schema.library.validator;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.ReflectionKeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.common.DependenciesValidator;
import xpertss.json.schema.keyword.validator.draftv4.AllOfValidator;
import xpertss.json.schema.keyword.validator.draftv4.AnyOfValidator;
import xpertss.json.schema.keyword.validator.draftv4.DraftV4TypeValidator;
import xpertss.json.schema.keyword.validator.draftv4.MaxPropertiesValidator;
import xpertss.json.schema.keyword.validator.draftv4.MinPropertiesValidator;
import xpertss.json.schema.keyword.validator.draftv4.MultipleOfValidator;
import xpertss.json.schema.keyword.validator.draftv4.NotValidator;
import xpertss.json.schema.keyword.validator.draftv4.OneOfValidator;
import xpertss.json.schema.keyword.validator.draftv4.RequiredKeywordValidator;

/**
 * Draft v4 specific keyword validator constructors
 */
public final class DraftV4ValidatorDictionary
{
    private static final Dictionary<KeywordValidatorFactory>
        DICTIONARY;

    private DraftV4ValidatorDictionary()
    {
    }

    public static Dictionary<KeywordValidatorFactory> get()
    {
        return DICTIONARY;
    }

    static {
        final DictionaryBuilder<KeywordValidatorFactory>
            builder = Dictionary.newBuilder();

        String keyword;
        Class<? extends KeywordValidator> c;

        builder.addAll(CommonValidatorDictionary.get());

        /*
         * Number/integer
         */
        keyword = "multipleOf";
        c = MultipleOfValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        /*
         * Object
         */
        keyword = "minProperties";
        c = MinPropertiesValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "maxProperties";
        c = MaxPropertiesValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "required";
        c = RequiredKeywordValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "dependencies";
        c = DependenciesValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        /*
         * All
         */
        keyword = "anyOf";
        c = AnyOfValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "allOf";
        c = AllOfValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "oneOf";
        c = OneOfValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "not";
        c = NotValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "type";
        c = DraftV4TypeValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        DICTIONARY = builder.freeze();
    }

    private static KeywordValidatorFactory factory(String name,
        final Class<? extends KeywordValidator> c)
    {
        return new ReflectionKeywordValidatorFactory(name, c);
    }
}
