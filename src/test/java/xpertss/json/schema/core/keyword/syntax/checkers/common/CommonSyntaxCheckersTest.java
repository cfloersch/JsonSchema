package xpertss.json.schema.core.keyword.syntax.checkers.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxCheckersTest;
import xpertss.json.schema.core.keyword.syntax.dictionaries.CommonSyntaxCheckerDictionary;

public abstract class CommonSyntaxCheckersTest extends SyntaxCheckersTest {
    
    protected CommonSyntaxCheckersTest(final String keyword)
        throws JsonProcessingException
    {
        super(CommonSyntaxCheckerDictionary.get(), "common", keyword);
    }
}
