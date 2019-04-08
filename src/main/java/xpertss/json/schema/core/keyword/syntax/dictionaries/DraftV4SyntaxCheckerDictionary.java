package xpertss.json.schema.core.keyword.syntax.dictionaries;

import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.DefinitionsSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.DraftV4DependenciesSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.DraftV4ItemsSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.DraftV4PropertiesSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.DraftV4TypeSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.NotSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.draftv4.RequiredSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.DivisorSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.PositiveIntegerSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaArraySyntaxChecker;

/**
 * Draft v4 specific syntax checkers
 */
public final class DraftV4SyntaxCheckerDictionary {

    private static final Dictionary<SyntaxChecker> DICTIONARY;

    public static Dictionary<SyntaxChecker> get()
    {
        return DICTIONARY;
    }

    private DraftV4SyntaxCheckerDictionary()
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
         * Array
         */
        keyword = "items";
        checker = DraftV4ItemsSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        /*
         * Integers and numbers
         */
        keyword = "multipleOf";
        checker = new DivisorSyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        /*
         * Objects
         */
        keyword = "minProperties";
        checker = new PositiveIntegerSyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "maxProperties";
        checker = new PositiveIntegerSyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "properties";
        checker = DraftV4PropertiesSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "required";
        checker = RequiredSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "dependencies";
        checker = DraftV4DependenciesSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        /*
         * All / metadata
         */
        keyword = "allOf";
        checker = new SchemaArraySyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "anyOf";
        checker = new SchemaArraySyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "oneOf";
        checker = new SchemaArraySyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "not";
        checker = NotSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "definitions";
        checker = DefinitionsSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "type";
        checker = DraftV4TypeSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        DICTIONARY = builder.freeze();
    }
}
