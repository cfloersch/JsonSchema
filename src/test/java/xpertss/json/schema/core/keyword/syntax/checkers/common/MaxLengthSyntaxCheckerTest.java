package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class MaxLengthSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public MaxLengthSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("maxLength");
    }
}
