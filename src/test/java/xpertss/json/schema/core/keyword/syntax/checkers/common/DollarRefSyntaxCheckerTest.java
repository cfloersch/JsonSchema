package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class DollarRefSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public DollarRefSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("$ref");
    }
}
