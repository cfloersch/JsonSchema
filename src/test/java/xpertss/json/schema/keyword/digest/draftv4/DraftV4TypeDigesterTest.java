package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DraftV4TypeDigesterTest
    extends DraftV4DigesterTest
{
    public DraftV4TypeDigesterTest()
        throws IOException
    {
        super("type", NodeType.ARRAY, NodeType.values());
    }
}
