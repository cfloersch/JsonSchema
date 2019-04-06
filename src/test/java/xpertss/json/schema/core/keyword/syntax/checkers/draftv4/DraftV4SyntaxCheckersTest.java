package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.core.JsonProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxCheckersTest;
import xpertss.json.schema.core.keyword.syntax.dictionaries.DraftV4SyntaxCheckerDictionary;

public abstract class DraftV4SyntaxCheckersTest
    extends SyntaxCheckersTest
{
    protected DraftV4SyntaxCheckersTest(final String keyword)
        throws JsonProcessingException
    {
        super(DraftV4SyntaxCheckerDictionary.get(), "draftv4", keyword);
    }
}
