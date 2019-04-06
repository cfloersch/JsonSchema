package xpertss.json.schema.keyword.validator.callback.draftv3;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.keyword.validator.callback.CallbackValidatorTest;
import xpertss.json.schema.library.validator.DraftV3ValidatorDictionary;

public abstract class DraftV3CallbackValidatorTest
    extends CallbackValidatorTest
{
    protected DraftV3CallbackValidatorTest(final String keyword,
        final JsonPointer ptr1, final JsonPointer ptr2)
    {
        super(DraftV3ValidatorDictionary.get(), keyword, ptr1, ptr2);
    }
}
