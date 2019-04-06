package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class MinItemsSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public MinItemsSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("minItems");
    }
}
