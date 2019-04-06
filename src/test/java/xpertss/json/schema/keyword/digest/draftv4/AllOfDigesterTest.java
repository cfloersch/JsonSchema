package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class AllOfDigesterTest
    extends DraftV4DigesterTest
{
    public AllOfDigesterTest()
        throws IOException
    {
        super("allOf", NodeType.ARRAY, NodeType.values());
    }
}
