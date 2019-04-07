package xpertss.json.schema.keyword.validator.draftv4;

import xpertss.json.schema.keyword.validator.AbstractKeywordValidatorTest;
import xpertss.json.schema.library.validator.DraftV4ValidatorDictionary;

import java.io.IOException;

public abstract class DraftV4KeywordValidatorTest extends AbstractKeywordValidatorTest {

    protected DraftV4KeywordValidatorTest(final String keyword)
        throws IOException
    {
        super(DraftV4ValidatorDictionary.get(), "draftv4", keyword);
    }
}
