package xpertss.json.schema.core.keyword.syntax.dictionaries;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.common.AdditionalSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.common.EnumSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.common.ExclusiveMaximumSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.common.ExclusiveMinimumSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.common.PatternPropertiesSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.common.PatternSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.PositiveIntegerSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.TypeOnlySyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.URISyntaxChecker;

import static com.github.fge.jackson.NodeType.*;

/**
 * Syntax checkers common to draft v4 and v3
 */
public final class CommonSyntaxCheckerDictionary
{
    private static final Dictionary<SyntaxChecker> DICTIONARY;

    public static Dictionary<SyntaxChecker> get()
    {
        return DICTIONARY;
    }

    private CommonSyntaxCheckerDictionary()
    {
    }

    static {
        final DictionaryBuilder<SyntaxChecker> dict = Dictionary.newBuilder();

        String keyword;
        SyntaxChecker checker;

        /*
         * Arrays
         */

        keyword = "additionalItems";
        checker = new AdditionalSyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "minItems";
        checker = new PositiveIntegerSyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "maxItems";
        checker = new PositiveIntegerSyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "uniqueItems";
        checker = new TypeOnlySyntaxChecker(keyword, BOOLEAN);
        dict.addEntry(keyword, checker);

        /*
         * Integers and numbers
         */
        keyword = "minimum";
        checker = new TypeOnlySyntaxChecker(keyword, INTEGER, NUMBER);
        dict.addEntry(keyword, checker);

        keyword = "exclusiveMinimum";
        checker = ExclusiveMinimumSyntaxChecker.getInstance();
        dict.addEntry(keyword, checker);

        keyword = "maximum";
        checker = new TypeOnlySyntaxChecker(keyword, INTEGER, NUMBER);
        dict.addEntry(keyword, checker);

        keyword = "exclusiveMaximum";
        checker = ExclusiveMaximumSyntaxChecker.getInstance();
        dict.addEntry(keyword, checker);

        /*
         * Objects
         */
        keyword = "additionalProperties";
        checker = new AdditionalSyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "patternProperties";
        checker = PatternPropertiesSyntaxChecker.getInstance();
        dict.addEntry(keyword, checker);

        keyword = "required";
        checker = new TypeOnlySyntaxChecker(keyword, BOOLEAN);
        dict.addEntry(keyword, checker);

        /*
         * Strings
         */
        keyword = "minLength";
        checker = new PositiveIntegerSyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "maxLength";
        checker = new PositiveIntegerSyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "pattern";
        checker = PatternSyntaxChecker.getInstance();
        dict.addEntry(keyword, checker);

        /*
         * All/metadata
         */
        keyword = "$schema";
        checker = new URISyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "$ref";
        checker = new URISyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "id";
        checker = new URISyntaxChecker(keyword);
        dict.addEntry(keyword, checker);

        keyword = "description";
        checker = new TypeOnlySyntaxChecker(keyword, STRING);
        dict.addEntry(keyword, checker);

        keyword = "title";
        checker = new TypeOnlySyntaxChecker(keyword, STRING);
        dict.addEntry(keyword, checker);

        keyword = "enum";
        checker = EnumSyntaxChecker.getInstance();
        dict.addEntry(keyword, checker);

        keyword = "format";
        checker = new TypeOnlySyntaxChecker(keyword, STRING);
        dict.addEntry(keyword, checker);

        // FIXME: we actually ignore this one
        keyword = "default";
        checker = new TypeOnlySyntaxChecker(keyword, ARRAY, values());
        dict.addEntry(keyword, checker);

        DICTIONARY = dict.freeze();
    }
}
