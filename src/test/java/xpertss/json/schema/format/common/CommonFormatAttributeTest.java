package xpertss.json.schema.format.common;

import xpertss.json.schema.format.AbstractFormatAttributeTest;
import xpertss.json.schema.library.format.CommonFormatAttributesDictionary;

import java.io.IOException;

public abstract class CommonFormatAttributeTest
    extends AbstractFormatAttributeTest
{
    protected CommonFormatAttributeTest(final String fmt)
        throws IOException
    {
        super(CommonFormatAttributesDictionary.get(), "common", fmt);
    }
}
