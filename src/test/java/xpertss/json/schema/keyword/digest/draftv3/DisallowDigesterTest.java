package xpertss.json.schema.keyword.digest.draftv3;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DisallowDigesterTest
    extends DraftV3DigesterTest
{
    public DisallowDigesterTest()
        throws IOException
    {
        super("disallow", NodeType.ARRAY, NodeType.values());
    }
}
