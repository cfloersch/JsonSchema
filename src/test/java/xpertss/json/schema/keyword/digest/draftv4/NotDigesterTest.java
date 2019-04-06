package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class NotDigesterTest
    extends DraftV4DigesterTest
{
    public NotDigesterTest()
        throws IOException
    {
        super("not", NodeType.ARRAY, NodeType.values());
    }
}
