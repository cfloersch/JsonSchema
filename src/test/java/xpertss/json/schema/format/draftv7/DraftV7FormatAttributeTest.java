package xpertss.json.schema.format.draftv7;

import xpertss.json.schema.format.AbstractFormatAttributeTest;
import xpertss.json.schema.library.format.DraftV7FormatAttributesDictionary;

import java.io.IOException;

public abstract class DraftV7FormatAttributeTest
    extends AbstractFormatAttributeTest
{
    protected DraftV7FormatAttributeTest(final String fmt)
        throws IOException
    {
        super(DraftV7FormatAttributesDictionary.get(), "draftv7", fmt);
    }
}
