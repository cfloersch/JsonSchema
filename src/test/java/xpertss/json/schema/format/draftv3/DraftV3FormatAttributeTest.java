package xpertss.json.schema.format.draftv3;

import xpertss.json.schema.format.AbstractFormatAttributeTest;
import xpertss.json.schema.library.format.DraftV3FormatAttributesDictionary;

import java.io.IOException;

public abstract class DraftV3FormatAttributeTest extends AbstractFormatAttributeTest {

    protected DraftV3FormatAttributeTest(final String fmt)
        throws IOException
    {
        super(DraftV3FormatAttributesDictionary.get(), "draftv3", fmt);
    }
}
