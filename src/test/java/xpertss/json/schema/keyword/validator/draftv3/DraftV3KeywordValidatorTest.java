package xpertss.json.schema.keyword.validator.draftv3;

import xpertss.json.schema.keyword.validator.AbstractKeywordValidatorTest;
import xpertss.json.schema.library.validator.DraftV3ValidatorDictionary;

import java.io.IOException;

public abstract class DraftV3KeywordValidatorTest
    extends AbstractKeywordValidatorTest
{
    protected DraftV3KeywordValidatorTest(final String keyword)
        throws IOException
    {
        super(DraftV3ValidatorDictionary.get(), "draftv3", keyword);
    }
}
