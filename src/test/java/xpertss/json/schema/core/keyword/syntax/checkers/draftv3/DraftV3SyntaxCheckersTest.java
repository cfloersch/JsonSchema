package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxCheckersTest;
import xpertss.json.schema.core.keyword.syntax.dictionaries.DraftV3SyntaxCheckerDictionary;

public abstract class DraftV3SyntaxCheckersTest
    extends SyntaxCheckersTest
{
    protected DraftV3SyntaxCheckersTest(final String keyword)
        throws JsonProcessingException
    {
        super(DraftV3SyntaxCheckerDictionary.get(), "draftv3", keyword);
    }
}
