package xpertss.json.schema.core.keyword.syntax.dictionaries;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv3.DraftV3DependenciesSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv3.DraftV3ItemsSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv3.DraftV3PropertiesSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv3.DraftV3TypeKeywordSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv3.ExtendsSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.DivisorSyntaxChecker;

/**
 * Draft v3 specific syntax checkers
 */
public final class DraftV3SyntaxCheckerDictionary {

    private static final Dictionary<SyntaxChecker> DICTIONARY;

    public static Dictionary<SyntaxChecker> get()
    {
        return DICTIONARY;
    }

    private DraftV3SyntaxCheckerDictionary()
    {
    }

    static {
        DictionaryBuilder<SyntaxChecker> builder = Dictionary.newBuilder();

        /*
         * Put all common checkers
         */
        builder.addAll(CommonSyntaxCheckerDictionary.get());

        String keyword;
        SyntaxChecker checker;

        /*
         * Draft v3 specific syntax checkers
         */

        /*
         * Arrays
         */
        keyword = "items";
        checker = DraftV3ItemsSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        /*
         * Numbers and integers
         */
        keyword = "divisibleBy";
        checker = new DivisorSyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        /*
         * Objects
         */
        keyword = "properties";
        checker = DraftV3PropertiesSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "dependencies";
        checker = DraftV3DependenciesSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        /*
         * All / metadata
         */
        keyword = "extends";
        checker = ExtendsSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "type";
        checker = new DraftV3TypeKeywordSyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "disallow";
        checker = new DraftV3TypeKeywordSyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        DICTIONARY = builder.freeze();
    }
}
