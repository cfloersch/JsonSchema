package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class NotSyntaxCheckerTest
    extends DraftV4SyntaxCheckersTest
{
    public NotSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("not");
    }
}
