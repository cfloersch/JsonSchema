package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class DollarSchemaSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public DollarSchemaSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("$schema");
    }
}
