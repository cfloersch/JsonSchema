package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class UniqueItemsSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public UniqueItemsSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("uniqueItems");
    }
}
