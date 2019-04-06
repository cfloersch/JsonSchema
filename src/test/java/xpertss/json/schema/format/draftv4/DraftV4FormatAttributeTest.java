package xpertss.json.schema.format.draftv4;

import xpertss.json.schema.format.AbstractFormatAttributeTest;
import xpertss.json.schema.library.format.DraftV4FormatAttributesDictionary;

import java.io.IOException;

public abstract class DraftV4FormatAttributeTest
    extends AbstractFormatAttributeTest
{
    protected DraftV4FormatAttributeTest(final String fmt)
        throws IOException
    {
        super(DraftV4FormatAttributesDictionary.get(), "draftv4", fmt);
    }
}
