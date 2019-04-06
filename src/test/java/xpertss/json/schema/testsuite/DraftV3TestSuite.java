package xpertss.json.schema.testsuite;

import xpertss.json.schema.SchemaVersion;

import java.io.IOException;

public final class DraftV3TestSuite
    extends TestSuite
{
    public DraftV3TestSuite()
        throws IOException
    {
        super(SchemaVersion.DRAFTV3, "draftv3");
    }
}
