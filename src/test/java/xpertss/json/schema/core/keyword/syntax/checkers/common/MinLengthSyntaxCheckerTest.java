package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class MinLengthSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public MinLengthSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("minLength");
    }
}
