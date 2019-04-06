package xpertss.json.schema.testsuite;

import xpertss.json.schema.SchemaVersion;

import java.io.IOException;

public final class DraftV4TestSuite
    extends TestSuite
{
    public DraftV4TestSuite()
        throws IOException
    {
        super(SchemaVersion.DRAFTV4, "draftv4");
    }
}
