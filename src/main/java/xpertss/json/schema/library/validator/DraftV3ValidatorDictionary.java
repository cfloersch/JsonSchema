package xpertss.json.schema.library.validator;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.ReflectionKeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.common.DependenciesValidator;
import xpertss.json.schema.keyword.validator.draftv3.DisallowKeywordValidator;
import xpertss.json.schema.keyword.validator.draftv3.DivisibleByValidator;
import xpertss.json.schema.keyword.validator.draftv3.DraftV3TypeValidator;
import xpertss.json.schema.keyword.validator.draftv3.ExtendsValidator;
import xpertss.json.schema.keyword.validator.draftv3.PropertiesValidator;

/**
 * Draft v3 specific keyword validator constructors
 */
public final class DraftV3ValidatorDictionary {

    private static final Dictionary<KeywordValidatorFactory> DICTIONARY;

    private DraftV3ValidatorDictionary()
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

        builder.addAll(CommonValidatorDictionary.get());

        /*
         * Number / integer
         */
        keyword = "divisibleBy";
        c = DivisibleByValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        /*
         * Object
         */
        keyword = "properties";
        c = PropertiesValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "dependencies";
        c = DependenciesValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "type";
        c = DraftV3TypeValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "disallow";
        c = DisallowKeywordValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        keyword = "extends";
        c = ExtendsValidator.class;
        builder.addEntry(keyword, factory(keyword, c));

        DICTIONARY = builder.freeze();
    }

    private static KeywordValidatorFactory factory(String name, Class<? extends KeywordValidator> c)
    {
        return new ReflectionKeywordValidatorFactory(name, c);
    }
}
