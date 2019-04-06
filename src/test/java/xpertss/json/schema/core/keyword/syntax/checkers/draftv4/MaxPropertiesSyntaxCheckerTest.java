package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class MaxPropertiesSyntaxCheckerTest
    extends DraftV4SyntaxCheckersTest
{
    public MaxPropertiesSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("maxProperties");
    }
}
