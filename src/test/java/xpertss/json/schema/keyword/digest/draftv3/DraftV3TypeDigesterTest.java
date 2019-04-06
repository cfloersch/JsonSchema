package xpertss.json.schema.keyword.digest.draftv3;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DraftV3TypeDigesterTest
    extends DraftV3DigesterTest
{
    public DraftV3TypeDigesterTest()
        throws IOException
    {
        super("type", NodeType.ARRAY, NodeType.values());
    }
}
