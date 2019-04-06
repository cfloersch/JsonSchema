package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class RequiredSyntaxCheckerTest
    extends DraftV4SyntaxCheckersTest
{
    public RequiredSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("required");
    }
}
