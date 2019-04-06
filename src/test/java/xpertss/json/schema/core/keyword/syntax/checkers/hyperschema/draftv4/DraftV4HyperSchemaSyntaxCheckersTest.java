package xpertss.json.schema.core.keyword.syntax.checkers.hyperschema.draftv4;

import com.fasterxml.jackson.core.JsonProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxCheckersTest;
import xpertss.json.schema.core.keyword.syntax.dictionaries.DraftV4HyperSchemaSyntaxCheckerDictionary;

public abstract class DraftV4HyperSchemaSyntaxCheckersTest
    extends SyntaxCheckersTest
{
    protected DraftV4HyperSchemaSyntaxCheckersTest(final String keyword)
        throws JsonProcessingException
    {
        super(DraftV4HyperSchemaSyntaxCheckerDictionary.get(),
            "hyperschema/draftv4", keyword);
    }
}
