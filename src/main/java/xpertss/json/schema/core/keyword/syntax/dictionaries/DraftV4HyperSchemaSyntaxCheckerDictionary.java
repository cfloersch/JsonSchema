package xpertss.json.schema.core.keyword.syntax.dictionaries;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.TypeOnlySyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.URISyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.hyperschema.LinksSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.hyperschema.MediaSyntaxChecker;

/**
 * Draft v4 hyperschema specific syntax checkers
 */
public final class DraftV4HyperSchemaSyntaxCheckerDictionary
{
    private static final Dictionary<SyntaxChecker> DICTIONARY;

    public static Dictionary<SyntaxChecker> get()
    {
        return DICTIONARY;
    }

    private DraftV4HyperSchemaSyntaxCheckerDictionary()
    {
    }

    static {
        final DictionaryBuilder<SyntaxChecker> builder
            = Dictionary.newBuilder();

        /*
         * Put all common checkers
         */
        builder.addAll(DraftV4SyntaxCheckerDictionary.get());

        String keyword;
        SyntaxChecker checker;

        keyword = "pathStart";
        checker = new URISyntaxChecker(keyword);
        builder.addEntry(keyword, checker);

        keyword = "fragmentResolution";
        checker = new TypeOnlySyntaxChecker(keyword, NodeType.STRING);
        builder.addEntry(keyword, checker);

        keyword = "media";
        checker = MediaSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        keyword = "links";
        checker = LinksSyntaxChecker.getInstance();
        builder.addEntry(keyword, checker);

        DICTIONARY = builder.freeze();
    }
}
