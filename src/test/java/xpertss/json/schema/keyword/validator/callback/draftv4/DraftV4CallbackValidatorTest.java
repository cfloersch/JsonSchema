package xpertss.json.schema.keyword.validator.callback.draftv4;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.keyword.validator.callback.CallbackValidatorTest;
import xpertss.json.schema.library.validator.DraftV4ValidatorDictionary;

public abstract class DraftV4CallbackValidatorTest
    extends CallbackValidatorTest
{
    protected DraftV4CallbackValidatorTest(final String keyword,
        final JsonPointer ptr1, final JsonPointer ptr2)
    {
        super(DraftV4ValidatorDictionary.get(), keyword, ptr1, ptr2);
    }
}
