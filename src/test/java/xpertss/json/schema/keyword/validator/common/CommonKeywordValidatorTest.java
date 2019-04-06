package xpertss.json.schema.keyword.validator.common;

import xpertss.json.schema.keyword.validator.AbstractKeywordValidatorTest;
import xpertss.json.schema.library.validator.CommonValidatorDictionary;

import java.io.IOException;

public abstract class CommonKeywordValidatorTest
    extends AbstractKeywordValidatorTest
{
    protected CommonKeywordValidatorTest(final String keyword)
        throws IOException
    {
        super(CommonValidatorDictionary.get(), "common", keyword);
    }
}
