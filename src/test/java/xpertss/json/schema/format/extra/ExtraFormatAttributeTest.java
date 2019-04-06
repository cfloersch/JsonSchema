package xpertss.json.schema.format.extra;

import xpertss.json.schema.format.AbstractFormatAttributeTest;
import xpertss.json.schema.library.format.ExtraFormatsDictionary;

import java.io.IOException;

public abstract class ExtraFormatAttributeTest
    extends AbstractFormatAttributeTest
{
    protected ExtraFormatAttributeTest(final String fmt)
        throws IOException
    {
        super(ExtraFormatsDictionary.get(), "extra", fmt);
    }
}
