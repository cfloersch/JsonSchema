package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class PatternSyntaxCheckerTest
    extends CommonSyntaxCheckersTest
{
    public PatternSyntaxCheckerTest()
        throws JsonProcessingException
    {
        super("pattern");
    }
}
