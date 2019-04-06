package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class DisallowSyntaxCheckerTest
    extends DraftV3SyntaxCheckersTest
{
    public DisallowSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("disallow");
    }
}
