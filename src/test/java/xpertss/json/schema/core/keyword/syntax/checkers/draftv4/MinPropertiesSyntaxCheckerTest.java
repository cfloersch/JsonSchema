package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class MinPropertiesSyntaxCheckerTest
    extends DraftV4SyntaxCheckersTest
{
    public MinPropertiesSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("minProperties");
    }
}
